package com.sc2002.arena.status;

import java.util.Random;

/**
 * Smoke Bomb effect — while active, the afflicted combatant has reduced
 * accuracy (miss chance).  The BattleEngine checks {@link #causedMiss()} when
 * resolving attacks against a target that is shrouded.
 */
public class SmokeBombEffect extends StatusEffect {

    /** Chance (0–100) that an attack against the shrouded combatant misses. */
    private static final int MISS_CHANCE_PERCENT = 50;

    private final Random random;

    public SmokeBombEffect(int duration) {
        this(duration, new Random());
    }

    /** Test-friendly constructor allowing a seeded {@link Random} to be injected. */
    public SmokeBombEffect(int duration, Random random) {
        super("Smoke Bomb", duration);
        this.random = random;
    }

    @Override
    public void onTurnStart() {
        // No per-turn damage; effect is checked during attack resolution.
    }

    /**
     * Randomly determines whether an incoming attack misses due to smoke cover.
     *
     * @return {@code true} if the attack misses
     */
    public boolean causedMiss() {
        return !isExpired() && random.nextInt(100) < MISS_CHANCE_PERCENT;
    }
}
