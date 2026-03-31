package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;

public class Potion implements Item {
    private final String name = "Health Potion";
    private final String description = "Restores HP to full.";

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
        int healAmount = target.getMaxHp() - target.getHp();
        target.heal(healAmount);
    }
}
