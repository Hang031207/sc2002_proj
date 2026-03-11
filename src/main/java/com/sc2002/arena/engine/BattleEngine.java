package com.sc2002.arena.engine;

import com.sc2002.arena.action.Action;
import com.sc2002.arena.action.BasicAttack;
import com.sc2002.arena.action.Defend;
import com.sc2002.arena.action.SpecialSkill;
import com.sc2002.arena.combatant.Combatant;
import com.sc2002.arena.combatant.Goblin;
import com.sc2002.arena.combatant.Wolf;
import com.sc2002.arena.status.SmokeBombEffect;
import com.sc2002.arena.strategy.SpeedBasedTurnOrder;
import com.sc2002.arena.strategy.TurnOrderStrategy;
import com.sc2002.arena.ui.GameUI;

import java.util.ArrayList;
import java.util.List;

/**
 * BattleEngine — orchestrates the game loop.
 *
 * <p>Responsibilities (Single Responsibility-aligned):
 * <ul>
 *   <li>Round management: determines turn order, processes each turn.</li>
 *   <li>Win/loss detection: checks whether the player or enemy party has been
 *       wiped out.</li>
 *   <li>Backup spawn hook: delegates to combatant subclasses that implement
 *       {@code checkBackupSpawn()} (Goblin, Wolf) and spawns reinforcements.</li>
 * </ul>
 *
 * <p>All I/O is delegated to {@link GameUI} — this class never writes to
 * {@code System.out} directly.
 */
public class BattleEngine {

    private final List<Combatant> players;
    private final List<Combatant> enemies;
    private final GameUI ui;
    private final TurnOrderStrategy turnOrderStrategy;

    /** Reusable action instances (stateless commands). */
    private static final BasicAttack  BASIC_ATTACK  = new BasicAttack();
    private static final Defend       DEFEND_ACTION = new Defend();
    private static final SpecialSkill SPECIAL_SKILL = new SpecialSkill();

    /** HP ratio below which an enemy will prefer defending over attacking. */
    private static final double ENEMY_DEFEND_HP_THRESHOLD    = 0.3;
    /** Probability (0–1) that an enemy uses its special skill on a given turn. */
    private static final double ENEMY_SPECIAL_SKILL_CHANCE   = 0.25;

    private int roundNumber = 0;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BattleEngine(List<Combatant> players, List<Combatant> enemies, GameUI ui) {
        this(players, enemies, ui, new SpeedBasedTurnOrder());
    }

    public BattleEngine(List<Combatant> players, List<Combatant> enemies,
                        GameUI ui, TurnOrderStrategy turnOrderStrategy) {
        this.players = new ArrayList<>(players);
        this.enemies = new ArrayList<>(enemies);
        this.ui = ui;
        this.turnOrderStrategy = turnOrderStrategy;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /** Runs the battle until one side is defeated. */
    public void start() {
        ui.displayBattleStart(players, enemies);

        while (isPlayerAlive() && isEnemyAlive()) {
            roundNumber++;
            runRound();
            handleBackupSpawns();
            removeDefeated();
        }

        ui.displayBattleEnd(isPlayerAlive() ? players : enemies, isPlayerAlive());
    }

    // -------------------------------------------------------------------------
    // Round logic
    // -------------------------------------------------------------------------

    private void runRound() {
        ui.displayRoundStart(roundNumber);

        List<Combatant> allCombatants = buildCombinedList();
        List<Combatant> turnOrder = turnOrderStrategy.orderTurns(allCombatants);

        for (Combatant actor : turnOrder) {
            if (!actor.isAlive()) continue;

            // Process status effects (Stun tick, Smoke Bomb tick, etc.)
            actor.processStatusEffects();

            if (actor.isStunned()) {
                ui.displayActionResult(new ActionResult(actor, null,
                        ActionResult.Outcome.STUN_SKIP, 0,
                        actor.getName() + " is stunned and cannot act!"));
                continue;
            }

            // Revert previous Defend bonus before this turn starts
            DEFEND_ACTION.revert(actor);

            // Revert Wolf Howl if applicable
            if (actor instanceof Wolf wolf) {
                wolf.revertHowl();
            }

            Combatant opponent = resolveOpponent(actor);
            if (opponent == null) continue;

            Action chosenAction = chooseAction(actor, opponent);
            ActionResult result = executeAction(chosenAction, actor, opponent);
            ui.displayActionResult(result);
        }

        ui.displayRoundEnd(roundNumber, players, enemies);
    }

    // -------------------------------------------------------------------------
    // Action selection
    // -------------------------------------------------------------------------

    /**
     * Determines which action an actor will use this turn.
     * Players choose via the UI; enemies use a simple AI heuristic.
     */
    private Action chooseAction(Combatant actor, Combatant opponent) {
        if (isPlayerCombatant(actor)) {
            int choice = ui.promptActionChoice(actor, List.of(BASIC_ATTACK, DEFEND_ACTION, SPECIAL_SKILL));
            return switch (choice) {
                case 1 -> BASIC_ATTACK;
                case 2 -> DEFEND_ACTION;
                case 3 -> SPECIAL_SKILL;
                default -> BASIC_ATTACK;
            };
        } else {
            return chooseEnemyAction(actor, opponent);
        }
    }

    /** Simple enemy AI: defend when low HP, otherwise attack or use special. */
    private Action chooseEnemyAction(Combatant actor, Combatant opponent) {
        double hpRatio = (double) actor.getCurrentHp() / actor.getMaxHp();
        if (hpRatio < ENEMY_DEFEND_HP_THRESHOLD) {
            return DEFEND_ACTION;
        }
        if (Math.random() < ENEMY_SPECIAL_SKILL_CHANCE) {
            return SPECIAL_SKILL;
        }
        return BASIC_ATTACK;
    }

    // -------------------------------------------------------------------------
    // Action execution with miss-detection
    // -------------------------------------------------------------------------

    private ActionResult executeAction(Action action, Combatant actor, Combatant target) {
        int hpBefore = (target != null) ? target.getCurrentHp() : actor.getCurrentHp();

        if (action instanceof BasicAttack) {
            // Check Smoke Bomb miss before delegating
            boolean missed = target != null && target.getStatusEffects().stream()
                    .filter(e -> e instanceof SmokeBombEffect)
                    .map(e -> (SmokeBombEffect) e)
                    .anyMatch(SmokeBombEffect::causedMiss);

            if (missed) {
                return new ActionResult(actor, target, ActionResult.Outcome.MISS, 0,
                        actor.getName() + " attacks " + target.getName()
                                + " but misses through the smoke!");
            }

            action.execute(actor, target);
            int damage = hpBefore - target.getCurrentHp();
            return new ActionResult(actor, target, ActionResult.Outcome.HIT, damage,
                    actor.getName() + " attacks " + target.getName()
                            + " for " + damage + " damage!");
        }

        if (action instanceof Defend) {
            action.execute(actor, target);
            return new ActionResult(actor, null, ActionResult.Outcome.DEFEND, 0,
                    actor.getName() + " takes a defensive stance (+DEF this turn).");
        }

        if (action instanceof SpecialSkill) {
            action.execute(actor, target);
            int effectiveDamage = (target != null) ? hpBefore - target.getCurrentHp() : 0;
            return new ActionResult(actor, target, ActionResult.Outcome.SPECIAL, effectiveDamage,
                    actor.getName() + " uses their special skill: "
                            + actor.getSpecialSkillDescription());
        }

        // Fallback
        action.execute(actor, target);
        return new ActionResult(actor, target, ActionResult.Outcome.HIT, 0,
                actor.getName() + " performs an action.");
    }

    // -------------------------------------------------------------------------
    // Backup spawn (placeholder — TODO: make configurable via factory/strategy)
    // -------------------------------------------------------------------------

    private void handleBackupSpawns() {
        List<Combatant> toAdd = new ArrayList<>();

        for (Combatant enemy : enemies) {
            if (enemy instanceof Goblin goblin && goblin.checkBackupSpawn()) {
                // TODO: inject a CombatantFactory to decouple spawn logic
                Goblin backup = new Goblin("Goblin (Backup)");
                toAdd.add(backup);
                ui.displayMessage(goblin.getName() + " cries for help! A backup Goblin appears!");
            }
            if (enemy instanceof Wolf wolf && wolf.checkBackupSpawn()) {
                // TODO: inject a CombatantFactory to decouple spawn logic
                Wolf backup = new Wolf("Wolf (Backup)");
                toAdd.add(backup);
                ui.displayMessage(wolf.getName() + " howls! A pack member joins the fight!");
            }
        }

        enemies.addAll(toAdd);
    }

    // -------------------------------------------------------------------------
    // Win/loss helpers
    // -------------------------------------------------------------------------

    public boolean isPlayerAlive() {
        return players.stream().anyMatch(Combatant::isAlive);
    }

    public boolean isEnemyAlive() {
        return enemies.stream().anyMatch(Combatant::isAlive);
    }

    // -------------------------------------------------------------------------
    // Utility helpers
    // -------------------------------------------------------------------------

    private void removeDefeated() {
        players.removeIf(c -> !c.isAlive());
        enemies.removeIf(c -> !c.isAlive());
    }

    private List<Combatant> buildCombinedList() {
        List<Combatant> all = new ArrayList<>(players);
        all.addAll(enemies);
        return all;
    }

    /** Returns the first living opponent for {@code actor}. */
    private Combatant resolveOpponent(Combatant actor) {
        if (isPlayerCombatant(actor)) {
            return enemies.stream().filter(Combatant::isAlive).findFirst().orElse(null);
        } else {
            return players.stream().filter(Combatant::isAlive).findFirst().orElse(null);
        }
    }

    private boolean isPlayerCombatant(Combatant c) {
        return players.contains(c);
    }

    // -------------------------------------------------------------------------
    // Accessors (for testing)
    // -------------------------------------------------------------------------

    public List<Combatant> getPlayers() { return List.copyOf(players); }
    public List<Combatant> getEnemies() { return List.copyOf(enemies); }
    public int getRoundNumber()         { return roundNumber; }
}
