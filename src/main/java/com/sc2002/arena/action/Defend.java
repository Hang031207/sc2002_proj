package com.sc2002.arena.action;

import com.sc2002.arena.combatant.Combatant;

/**
 * Defend — the actor braces for impact, gaining a temporary Defence bonus this
 * round.  The bonus is reverted by the BattleEngine at the start of the next
 * turn via {@link #revert(Combatant)}.
 */
public class Defend implements Action {

    /** Flat Defence bonus granted while defending. */
    private static final int DEFENSE_BONUS = 8;

    @Override
    public void execute(Combatant actor, Combatant target) {
        actor.applyDefenseBonus(DEFENSE_BONUS);
    }

    /**
     * Removes the temporary Defence bonus at the end of the defending round.
     *
     * @param actor the combatant who previously defended
     */
    public void revert(Combatant actor) {
        actor.removeDefenseBonus(DEFENSE_BONUS);
    }

    /** Returns the flat defence bonus this action provides. */
    public int getDefenseBonus() {
        return DEFENSE_BONUS;
    }

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public String getDescription() {
        return "Increases DEF by " + DEFENSE_BONUS + " until the start of your next turn.";
    }
}
