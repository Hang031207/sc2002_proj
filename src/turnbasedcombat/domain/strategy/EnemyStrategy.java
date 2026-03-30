package turnbasedcombat.domain.strategy;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.action.Action;
import java.util.List;

public interface EnemyStrategy {
    Action decideAction(Combatant self, List<Combatant> allies, List<Combatant> enemies);
    Combatant selectTarget(Combatant self, List<Combatant> possibleTargets);
}
