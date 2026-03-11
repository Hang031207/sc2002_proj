package com.sc2002.arena.combatant;

/**
 * Goblin — a nimble enemy with high Speed but low HP and Defence.
 *
 * <p>Special skill: <b>Dirty Trick</b> — applies a Smoke Bomb effect on the
 * target, reducing the target's accuracy for 1 turn (flavour: throws sand in
 * the player's eyes).
 *
 * <p>Backup spawn placeholder: if the Goblin's HP drops below 25 %, it may
 * call for reinforcements ({@link #checkBackupSpawn()}).
 */
public class Goblin extends Combatant {

    private static final int BASE_HP      = 50;
    private static final int BASE_ATTACK  = 12;
    private static final int BASE_DEFENSE = 4;
    private static final int BASE_SPEED   = 16;

    private static final double BACKUP_SPAWN_HP_THRESHOLD = 0.25;
    private boolean backupSpawnTriggered = false;

    public Goblin(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillDescription() {
        return "Dirty Trick — applies a 1-turn Smoke Bomb effect on the target.";
    }

    @Override
    public void performSpecialSkill(Combatant target) {
        if (target == null) return;
        target.addStatusEffect(new com.sc2002.arena.status.SmokeBombEffect(1));
    }

    // -------------------------------------------------------------------------
    // Backup Spawn — placeholder logic (TODO: integrate with BattleEngine)
    // -------------------------------------------------------------------------

    /**
     * Checks whether backup spawn conditions are met.
     * Called by the BattleEngine at the end of each round.
     *
     * @return {@code true} the first time HP falls below the threshold;
     *         {@code false} on subsequent calls or if threshold not reached.
     */
    public boolean checkBackupSpawn() {
        if (!backupSpawnTriggered
                && (double) getCurrentHp() / getMaxHp() < BACKUP_SPAWN_HP_THRESHOLD) {
            backupSpawnTriggered = true;
            return true; // TODO: BattleEngine should spawn a new Goblin
        }
        return false;
    }
}
