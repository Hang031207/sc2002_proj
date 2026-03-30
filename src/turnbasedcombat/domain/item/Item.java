package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;

public interface Item {
    String getName();
    String getDescription();
    void use(Combatant user, Combatant target);
}
