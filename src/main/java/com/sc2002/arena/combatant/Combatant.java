package com.sc2002.arena.combatant;

import com.sc2002.arena.status.StatusEffect;
import com.sc2002.arena.status.StunEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base for every participant in a battle.
 *
 * <p>Follows the Template Method pattern: subclasses fill in
 * {@link #getSpecialSkillDescription()} and {@link #performSpecialSkill(Combatant)}
 * while the common combat attributes and status-effect machinery live here.
 */
public abstract class Combatant {

    private final String name;
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private int speed;

    private final List<StatusEffect> statusEffects = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    protected Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = hp;
        this.currentHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    // -------------------------------------------------------------------------
    // Core accessors
    // -------------------------------------------------------------------------

    public String getName()      { return name; }
    public int getMaxHp()        { return maxHp; }
    public int getCurrentHp()    { return currentHp; }
    public int getAttack()       { return attack; }
    public int getDefense()      { return defense; }
    public int getSpeed()        { return speed; }

    public boolean isAlive()     { return currentHp > 0; }

    // -------------------------------------------------------------------------
    // Combat helpers
    // -------------------------------------------------------------------------

    /**
     * Applies damage after defence reduction.  Damage cannot reduce HP below 0.
     *
     * @param rawDamage the attacker's raw damage value (before defence)
     * @return actual HP lost
     */
    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(0, rawDamage - defense);
        currentHp = Math.max(0, currentHp - actualDamage);
        return actualDamage;
    }

    /**
     * Heals the combatant by {@code amount} HP, capped at max HP.
     *
     * @param amount HP to restore
     */
    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    /**
     * Temporarily increases the defence stat (e.g. when the Defend action is used).
     *
     * @param bonus defence points to add
     */
    public void applyDefenseBonus(int bonus) {
        defense += bonus;
    }

    /**
     * Removes a previously applied defence bonus.
     *
     * @param bonus defence points to remove
     */
    public void removeDefenseBonus(int bonus) {
        defense = Math.max(0, defense - bonus);
    }

    // -------------------------------------------------------------------------
    // Status-effect management
    // -------------------------------------------------------------------------

    /** Adds a status effect to this combatant. */
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
    }

    /** Returns an unmodifiable view of the current status effects. */
    public List<StatusEffect> getStatusEffects() {
        return List.copyOf(statusEffects);
    }

    /**
     * Processes all active status effects at the start of a turn, ticks their
     * durations down, and removes any that have expired.
     */
    public void processStatusEffects() {
        statusEffects.forEach(e -> {
            e.onTurnStart();
            e.tick();
        });
        statusEffects.removeIf(StatusEffect::isExpired);
    }

    /**
     * Returns {@code true} if the combatant is currently stunned and therefore
     * cannot act this turn.
     */
    public boolean isStunned() {
        return statusEffects.stream()
                .filter(e -> e instanceof StunEffect)
                .map(e -> (StunEffect) e)
                .anyMatch(StunEffect::preventsAction);
    }

    // -------------------------------------------------------------------------
    // Abstract contract for subclasses
    // -------------------------------------------------------------------------

    /** A short description of this combatant's special skill. */
    public abstract String getSpecialSkillDescription();

    /**
     * Executes the special skill targeting {@code target}.
     *
     * @param target the opponent (may be {@code null} for self-targeting skills)
     */
    public abstract void performSpecialSkill(Combatant target);

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    /** Returns a formatted status line suitable for the battle UI. */
    public String getStatusLine() {
        return String.format("%s  HP:%d/%d  ATK:%d  DEF:%d  SPD:%d",
                name, currentHp, maxHp, attack, defense, speed);
    }

    @Override
    public String toString() {
        return getStatusLine();
    }
}
