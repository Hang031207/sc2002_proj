package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public interface Action {
    String getName();
    void execute(Combatant actor, List<Combatant> targets);
}
