package turnbasedcombat.domain.strategy;

import turnbasedcombat.domain.character.Combatant;

import java.util.List;

import turnbasedcombat.control.BattleEngine;

public interface EnemyStrategy {
    void executeTurn(Combatant enemy, BattleEngine engine);
    public Action decideAction(Combatant self, List<Combatant> allies, List<Combatant> enemies);
    public Combatant selectTarget(Combatant self, List<Combatant> possibleTargets);
}
