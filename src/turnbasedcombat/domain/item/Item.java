package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public interface Item {
    String getName();
    String getDescription();
    void useItem(Combatant user, List<Combatant> targets);
}
