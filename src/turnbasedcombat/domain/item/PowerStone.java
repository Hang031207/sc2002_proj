package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;

public class PowerStone implements Item {
    private final String name = "Power Stone";
    private final String description = "Trigger the character's special skill for free without changing the cooldown timer.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void use(Combatant user, Combatant target) {
        
    }
}
