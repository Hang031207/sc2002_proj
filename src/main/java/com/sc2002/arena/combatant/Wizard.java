package com.sc2002.arena.combatant;

/**
 * Wizard — a glass-cannon mage with high Attack and Speed but low Defence.
 *
 * <p>Special skill: <b>Smoke Bomb</b> — throws a smoke bomb that shrouds the
 * Wizard, causing incoming attacks to have a 50% chance to miss for 2 turns.
 */
public class Wizard extends Combatant {

    private static final int BASE_HP      = 80;
    private static final int BASE_ATTACK  = 24;
    private static final int BASE_DEFENSE = 5;
    private static final int BASE_SPEED   = 14;

    /** Duration of the Smoke Bomb evasion effect (in turns). */
    private static final int SMOKE_BOMB_DURATION = 2;

    public Wizard(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillDescription() {
        return "Smoke Bomb — grants " + SMOKE_BOMB_DURATION
                + " turn(s) of 50% miss chance to incoming attacks.";
    }

    /**
     * Smoke Bomb: applies a {@link com.sc2002.arena.status.SmokeBombEffect}
     * to the caster (self-targeting).
     */
    @Override
    public void performSpecialSkill(Combatant target) {
        // Self-targeting: ignore the target parameter.
        addStatusEffect(new com.sc2002.arena.status.SmokeBombEffect(SMOKE_BOMB_DURATION));
    }
}
