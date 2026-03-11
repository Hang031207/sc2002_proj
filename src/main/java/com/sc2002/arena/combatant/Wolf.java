package com.sc2002.arena.combatant;

/**
 * Wolf — a balanced enemy that is strong and fast.
 *
 * <p>Special skill: <b>Howl</b> — buffs the Wolf's own Attack stat by 5 for
 * 1 round (reverted automatically at the start of the next round).
 *
 * <p>Backup spawn placeholder: if the Wolf is the last remaining enemy, it may
 * call the pack ({@link #checkBackupSpawn()}).
 */
public class Wolf extends Combatant {

    private static final int BASE_HP      = 70;
    private static final int BASE_ATTACK  = 15;
    private static final int BASE_DEFENSE = 7;
    private static final int BASE_SPEED   = 13;

    private static final int HOWL_ATTACK_BONUS = 5;
    private boolean howlActive = false;
    private boolean backupSpawnTriggered = false;

    public Wolf(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillDescription() {
        return "Howl — increases own Attack by " + HOWL_ATTACK_BONUS + " for 1 round.";
    }

    /**
     * Howl: temporarily boosts the Wolf's Attack.
     * The buff is tracked via {@link #howlActive} and should be reverted by
     * calling {@link #revertHowl()} at the start of the next turn.
     */
    @Override
    public void performSpecialSkill(Combatant target) {
        if (!howlActive) {
            applyDefenseBonus(0); // no-op; kept for symmetry
            // Directly boost attack via accessor (workaround: re-assign via helper)
            applyAttackBuff(HOWL_ATTACK_BONUS);
            howlActive = true;
        }
    }

    /**
     * Reverts the Howl attack bonus.
     * Called by the BattleEngine at the end of the round in which Howl was used.
     */
    public void revertHowl() {
        if (howlActive) {
            applyAttackBuff(-HOWL_ATTACK_BONUS);
            howlActive = false;
        }
    }

    /** Returns whether the Howl buff is currently active. */
    public boolean isHowlActive() {
        return howlActive;
    }

    // -------------------------------------------------------------------------
    // Backup Spawn — placeholder logic (TODO: integrate with BattleEngine)
    // -------------------------------------------------------------------------

    /**
     * Checks whether the Wolf should call pack reinforcements.
     *
     * @return {@code true} the first time HP drops to 0 (i.e., Wolf is defeated);
     *         intended to be checked just before removal from the battle.
     */
    public boolean checkBackupSpawn() {
        if (!backupSpawnTriggered && !isAlive()) {
            backupSpawnTriggered = true;
            return true; // TODO: BattleEngine should spawn a backup Wolf
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Applies a direct attack bonus (positive or negative).
     * Because reflection on the parent's field is not feasible here, we track
     * cumulative changes via a dedicated delta stored in this subclass.
     *
     * <p><em>Note:</em> Combatant exposes defence helpers; attack buffing is
     * handled here until a more general buff system is added.
     */
    private int attackDelta = 0;

    private void applyAttackBuff(int delta) {
        attackDelta += delta;
        // The Combatant base class does not expose a setAttack; we shadow it
        // with a local delta and override getAttack() to account for it.
    }

    /**
     * Returns effective attack, including any active Howl bonus.
     */
    @Override
    public int getAttack() {
        return super.getAttack() + attackDelta;
    }
}
