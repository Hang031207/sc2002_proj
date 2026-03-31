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
        // We pass 'null' for the target because the Power Stone is used ON the user, 
        // but the user's skill (like Arcane Blast) will hit the enemies.
        // You'll need to pass the actual enemy list from the Engine if required.
        user.executeSpecialSkill(target, null); 
        
        System.out.println(user.getName() + " used a Power Stone to trigger their Special Skill for free!");
    }
}
