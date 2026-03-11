package com.sc2002.arena.strategy;

import com.sc2002.arena.combatant.Combatant;

import java.util.List;

/**
 * Strategy Pattern — encapsulates the algorithm for ordering combatant turns.
 *
 * <p>Concrete implementations can sort by Speed, reverse order, random order,
 * etc., without changing the BattleEngine.
 */
public interface TurnOrderStrategy {

    /**
     * Returns a new list of combatants arranged in the order they should act
     * for the upcoming round.
     *
     * @param combatants all living participants in the current battle
     * @return ordered list (first element acts first)
     */
    List<Combatant> orderTurns(List<Combatant> combatants);
}
