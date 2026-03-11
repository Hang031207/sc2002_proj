package com.sc2002.arena.status;

/**
 * Represents a status effect that can be applied to a Combatant.
 * Follows the Open/Closed Principle — new effects extend this class.
 */
public abstract class StatusEffect {

    private final String name;
    private int remainingDuration; // number of turns the effect lasts

    protected StatusEffect(String name, int duration) {
        this.name = name;
        this.remainingDuration = duration;
    }

    /** Returns the display name of this effect. */
    public String getName() {
        return name;
    }

    /** Returns how many turns remain for this effect. */
    public int getRemainingDuration() {
        return remainingDuration;
    }

    /** @return {@code true} if the effect has expired. */
    public boolean isExpired() {
        return remainingDuration <= 0;
    }

    /**
     * Called at the start of the affected combatant's turn.
     * Subclasses override this to apply per-turn behaviour.
     */
    public abstract void onTurnStart();

    /**
     * Ticks down the duration by one turn.
     * Should be called after {@link #onTurnStart()}.
     */
    public void tick() {
        if (remainingDuration > 0) {
            remainingDuration--;
        }
    }

    @Override
    public String toString() {
        return name + "(" + remainingDuration + " turn(s) left)";
    }
}
