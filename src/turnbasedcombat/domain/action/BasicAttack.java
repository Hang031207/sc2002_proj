package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.strategy.EnemyStrategy;

import java.util.List;

public class BasicAttack implements Action {
    private final String name = "Basic Attack";
    private final String description = "A standard attack dealing damage based on attack power.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void execute(Combatant performer, List<Combatant> allCombatants) {
        int damage = performer.getAttack();

        EnemyStrategy strategy = new BasicAttackStrategy();
        Combatant target = strategy.selectTarget(allCombatants);

        target.takeDamage(damage);
    }

    @Override
    public boolean canExecute(Combatant performer) {
        return performer.isAlive();
    }
}
