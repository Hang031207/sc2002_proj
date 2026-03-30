package turnbasedcombat.control;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.domain.effect.StatusEffect;
import turnbasedcombat.domain.effect.StunEffect;
import turnbasedcombat.domain.strategy.EnemyStrategy;
import turnbasedcombat.boundary.GameCLI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BattleEngine {
    private final List<Combatant> playerTeam;
    private final List<Combatant> enemyTeam;
    private final TurnOrderStrategy turnOrderStrategy;
    private final GameCLI cli;
    private final Map<Combatant, EnemyStrategy> enemyStrategies;
    private int turnCount;

    public BattleEngine(List<Combatant> playerTeam, List<Combatant> enemyTeam,
                        TurnOrderStrategy turnOrderStrategy, GameCLI cli) {
        this.playerTeam = new ArrayList<>(playerTeam);
        this.enemyTeam = new ArrayList<>(enemyTeam);
        this.turnOrderStrategy = turnOrderStrategy;
        this.cli = cli;
        this.enemyStrategies = new HashMap<>();
        this.turnCount = 0;
    }

    public void setEnemyStrategy(Combatant enemy, EnemyStrategy strategy) {
        enemyStrategies.put(enemy, strategy);
    }

    public boolean runBattle() {
        cli.displayBattleStart(playerTeam, enemyTeam);

        while (!isBattleOver()) {
            turnCount++;
            cli.displayTurnStart(turnCount);

            List<Combatant> allCombatants = getAllCombatants();
            List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(allCombatants);

            for (Combatant combatant : turnOrder) {
                if (!combatant.isAlive()) {
                    continue;
                }

                if (isStunned(combatant)) {
                    cli.displayStunned(combatant);
                    combatant.tickStatusEffects();
                    continue;
                }

                executeTurn(combatant, allCombatants);

                if (isBattleOver()) {
                    break;
                }
            }

            cli.displayTurnEnd(playerTeam, enemyTeam);
        }

        boolean playerWon = isTeamAlive(playerTeam);
        cli.displayBattleEnd(playerWon);
        return playerWon;
    }

    private void executeTurn(Combatant combatant, List<Combatant> allCombatants) {
        cli.displayCombatantTurn(combatant);

        Action action;
        Combatant target;

        if (playerTeam.contains(combatant)) {
            action = cli.getPlayerAction(combatant);
            target = cli.getPlayerTarget(combatant, action, enemyTeam, playerTeam);
        } else {
            EnemyStrategy strategy = enemyStrategies.get(combatant);
            if (strategy != null) {
                action = strategy.decideAction(combatant, enemyTeam, playerTeam);
                target = strategy.selectTarget(combatant, playerTeam);
            } else {
                return;
            }
        }

        if (target != null && action.canExecute(combatant)) {
            cli.displayActionExecution(combatant, action, target);
            action.execute(combatant, target, allCombatants);
            cli.displayActionResult(combatant, action, target);
        }

        combatant.tickStatusEffects();
    }

    private boolean isStunned(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (effect instanceof StunEffect) {
                return true;
            }
        }
        return false;
    }

    private List<Combatant> getAllCombatants() {
        List<Combatant> all = new ArrayList<>(playerTeam);
        all.addAll(enemyTeam);
        return all;
    }

    private boolean isBattleOver() {
        return !isTeamAlive(playerTeam) || !isTeamAlive(enemyTeam);
    }

    private boolean isTeamAlive(List<Combatant> team) {
        for (Combatant c : team) {
            if (c.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public int getTurnCount() {
        return turnCount;
    }
}
