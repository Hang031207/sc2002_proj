package com.sc2002.arena.action;

import com.sc2002.arena.combatant.Combatant;
import com.sc2002.arena.status.SmokeBombEffect;

/**
 * Basic Attack — deals damage equal to the actor's Attack stat, reduced by
 * the target's Defence.  If the target is under a
 * {@link SmokeBombEffect} the attack may miss.
 */
public class BasicAttack implements Action {

    @Override
    public void execute(Combatant actor, Combatant target) {
        if (target == null || !target.isAlive()) return;

        // Check Smoke Bomb miss chance on the target
        boolean missed = target.getStatusEffects().stream()
                .filter(e -> e instanceof SmokeBombEffect)
                .map(e -> (SmokeBombEffect) e)
                .anyMatch(SmokeBombEffect::causedMiss);

        if (missed) {
            // Miss is communicated via a result object or UI — engine reads actor/target
            return;
        }

        target.takeDamage(actor.getAttack());
    }

    @Override
    public String getName() {
        return "Basic Attack";
    }

    @Override
    public String getDescription() {
        return "Deals ATK damage to the opponent (reduced by target's DEF).";
    }
}
