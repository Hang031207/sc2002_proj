package com.sc2002.arena.engine;

import com.sc2002.arena.combatant.Combatant;

/**
 * Immutable value object that records what happened during a single action
 * within a turn.  The BattleEngine populates this; the GameUI renders it.
 */
public final class ActionResult {

    public enum Outcome { HIT, MISS, DEFEND, SPECIAL, STUN_SKIP }

    private final Combatant actor;
    private final Combatant target;
    private final Outcome outcome;
    private final int damageDealt;
    private final String message;

    public ActionResult(Combatant actor, Combatant target,
                        Outcome outcome, int damageDealt, String message) {
        this.actor = actor;
        this.target = target;
        this.outcome = outcome;
        this.damageDealt = damageDealt;
        this.message = message;
    }

    public Combatant getActor()     { return actor; }
    public Combatant getTarget()    { return target; }
    public Outcome   getOutcome()   { return outcome; }
    public int       getDamageDealt() { return damageDealt; }
    public String    getMessage()   { return message; }

    @Override
    public String toString() {
        return message;
    }
}
