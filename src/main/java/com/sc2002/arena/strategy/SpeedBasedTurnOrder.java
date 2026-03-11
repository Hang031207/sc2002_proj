package com.sc2002.arena.strategy;

import com.sc2002.arena.combatant.Combatant;

import java.util.Comparator;
import java.util.List;

/**
 * Speed-based turn order — combatants with higher Speed act first.
 * Ties are broken alphabetically by name for determinism.
 */
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> orderTurns(List<Combatant> combatants) {
        return combatants.stream()
                .sorted(Comparator.comparingInt(Combatant::getSpeed).reversed()
                        .thenComparing(Combatant::getName))
                .toList();
    }
}
