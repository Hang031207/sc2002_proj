package turnbasedcombat.domain.character;

import turnbasedcombat.control.BattleEngine;

public class Warrior extends Combatant {

    public Warrior(String name) {
        super(name, 260, 40, 20, 30);
    }

    public Warrior() {
        this("Warrior");
    }

    @Override
    public String getSpecialSkillName() {
        return "Shield Bash";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "A powerful bash that deals damage and stun the target.";
    }

    @Override
    public void executeSpecialSkill(Combatant target, List<Combatant> allEnemies) {
        // Implementation of Shield Bash effect
        int damage = this.getAttack();
        target.takeDamage(damage);
        target.addStatusEffect(new StunEffect(2)); // Stun for 2 turns
    }

    @Override
    public void takeTurn(BattleEngine engine){}

}
