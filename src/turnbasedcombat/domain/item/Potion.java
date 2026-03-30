package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;

public class Potion implements Item {
    private final String name = "Health Potion";
    private final String description = "Restores 30 HP to the target.";
    private final int healAmount = 30;

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
        target.heal(healAmount);
    }
}
