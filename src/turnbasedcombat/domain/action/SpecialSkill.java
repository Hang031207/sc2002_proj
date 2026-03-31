package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public class SpecialSkill implements Action {

    @Override
    public String getName() {
        return "Special Skill";
    }

    @Override
    public String getDescription() {
        return "Use the character's unique special ability.";
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allCombatants) {
        
    }

  
    @Override
    public boolean canExecute(Combatant performer) {
        return performer.isAlive();
    }
}
