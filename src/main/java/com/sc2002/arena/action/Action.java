package com.sc2002.arena.action;

import com.sc2002.arena.combatant.Combatant;

/**
 * Command Pattern — each concrete {@code Action} encapsulates one combat move.
 *
 * <p>The {@link com.sc2002.arena.engine.BattleEngine} calls
 * {@link #execute(Combatant, Combatant)} without knowing the details of the
 * action, keeping the engine logic decoupled from specific move implementations.
 */
public interface Action {

    /**
     * Executes this action.
     *
     * @param actor  the combatant performing the action
     * @param target the opponent (may be ignored for self-targeting actions)
     */
    void execute(Combatant actor, Combatant target);

    /** Human-readable name of this action (shown in the UI menu). */
    String getName();

    /** Short description of what this action does. */
    String getDescription();
}
