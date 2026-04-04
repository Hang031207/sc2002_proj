package turnbasedcombat.domain.character;

import java.util.List;

import turnbasedcombat.control.BattleEngine;
import turnbasedcombat.domain.effect.ArcaneBlastBoost;

public class Wizard extends Combatant {

    public Wizard(String name) {
        super(name, 200, 50, 10, 20);
    }

    public Wizard() {
        this("Wizard");
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "A powerful magical attack that deals heavy damage and boosts next attack.";
    }

    @Override
    public void executeSpecialSkill(Combatant target, List<Combatant> allEnemies) {
        // Assume allEnemies is already the list of surviving enemies, deal damage to all of them
        int damage = this.getAttack(); // Base damage plus bonus
        for (Combatant c : allEnemies) {
            c.takeDamage(damage);
        }
        
        this.addStatusEffect(new ArcaneBlastBoost(1,10)); // Not sure what does 'lasting until end of the level mean'
    }

    @Override
    public void takeTurn(BattleEngine engine){}
}
