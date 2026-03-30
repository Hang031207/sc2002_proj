package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.effect.SmokeBombEffect;

public class SmokeBomb implements Item {
    private final String name = "Smoke Bomb";
    private final String description = "Creates a smoke screen, increasing evasion for 2 turns.";
    private final int evasionBoost = 5;
    private final int duration = 2;

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
        target.addStatusEffect(new SmokeBombEffect(duration, evasionBoost));
    }
}
