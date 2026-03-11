package com.sc2002.arena.combatant;

/**
 * Warrior — a melee fighter with high HP and Defence.
 *
 * <p>Special skill: <b>Shield Bash</b> — deals moderate damage and stuns the
 * target for 1 turn.
 */
public class Warrior extends Combatant {

    private static final int BASE_HP      = 120;
    private static final int BASE_ATTACK  = 18;
    private static final int BASE_DEFENSE = 12;
    private static final int BASE_SPEED   = 8;

    /** Stun duration applied by Shield Bash (in turns). */
    private static final int SHIELD_BASH_STUN_DURATION = 1;
    /** Damage multiplier for Shield Bash relative to base attack. */
    private static final double SHIELD_BASH_DAMAGE_MULT = 0.8;

    public Warrior(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillDescription() {
        return "Shield Bash — deals " + (int)(BASE_ATTACK * SHIELD_BASH_DAMAGE_MULT)
                + " damage and stuns the target for " + SHIELD_BASH_STUN_DURATION + " turn(s).";
    }

    /**
     * Shield Bash: deals reduced damage and applies a {@link com.sc2002.arena.status.StunEffect}
     * to the target.
     */
    @Override
    public void performSpecialSkill(Combatant target) {
        if (target == null) return;

        int damage = (int)(getAttack() * SHIELD_BASH_DAMAGE_MULT);
        target.takeDamage(damage);
        target.addStatusEffect(new com.sc2002.arena.status.StunEffect(SHIELD_BASH_STUN_DURATION));
    }
}
