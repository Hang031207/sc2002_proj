package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;

public class PowerStone implements Item {
    private final String name = "Power Stone";
    private final String description = "Temporarily increases attack power by 10 for the rest of the battle.";
    private final int attackBoost = 10;

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
        target.modifyAttack(attackBoost);
    }
}
