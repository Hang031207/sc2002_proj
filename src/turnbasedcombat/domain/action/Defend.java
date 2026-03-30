package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.effect.DefendEffect;
import java.util.List;

public class Defend implements Action {
    private final String name = "Defend";
    private final String description = "Take a defensive stance, reducing incoming damage by half until next turn.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allCombatants) {
        DefendEffect defendEffect = new DefendEffect(1);
        performer.addStatusEffect(defendEffect);
    }

    @Override
    public boolean canExecute(Combatant performer) {
        return performer.isAlive();
    }
}
