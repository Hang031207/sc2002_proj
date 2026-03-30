package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.character.Warrior;
import turnbasedcombat.domain.character.Wizard;
import turnbasedcombat.domain.effect.StunEffect;
import turnbasedcombat.domain.effect.ArcaneBlastBoost;
import java.util.List;
import java.util.Random;

public class SpecialSkill implements Action {
    private final Random random = new Random();

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
        int manaCost = performer.getSpecialSkillManaCost();
        performer.useMana(manaCost);

        if (performer instanceof Warrior) {
            executeShieldBash(performer, target);
        } else if (performer instanceof Wizard) {
            executeArcaneBlast(performer, target);
        } else {
            executeGenericSpecial(performer, target);
        }
    }

    private void executeShieldBash(Combatant performer, Combatant target) {
        int damage = (int) (performer.getAttack() * 1.5);
        target.takeDamage(damage);

        if (random.nextInt(100) < 30) {
            target.addStatusEffect(new StunEffect(1));
        }
    }

    private void executeArcaneBlast(Combatant performer, Combatant target) {
        int damage = (int) (performer.getAttack() * 2.0);
        target.takeDamage(damage);

        performer.addStatusEffect(new ArcaneBlastBoost(2, 5));
    }

    private void executeGenericSpecial(Combatant performer, Combatant target) {
        int damage = (int) (performer.getAttack() * 1.3);
        target.takeDamage(damage);
    }

    @Override
    public boolean canExecute(Combatant performer) {
        return performer.isAlive() && performer.getMana() >= performer.getSpecialSkillManaCost();
    }
}
