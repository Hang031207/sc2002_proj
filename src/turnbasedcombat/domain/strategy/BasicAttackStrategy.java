package turnbasedcombat.domain.strategy;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.domain.action.BasicAttack;
import java.util.List;
import java.util.Random;

public class BasicAttackStrategy implements EnemyStrategy {
    private final Random random = new Random();
    private final Action basicAttack = new BasicAttack();

    @Override
    public Action decideAction(Combatant self, List<Combatant> allies, List<Combatant> enemies) {
        return basicAttack;
    }

    @Override
    public Combatant selectTarget(Combatant self, List<Combatant> possibleTargets) {
        List<Combatant> aliveTargets = possibleTargets.stream()
                .filter(Combatant::isAlive)
                .toList();

        if (aliveTargets.isEmpty()) {
            return null;
        }

        Combatant lowestHpTarget = aliveTargets.get(0);
        for (Combatant target : aliveTargets) {
            if (target.getHp() < lowestHpTarget.getHp()) {
                lowestHpTarget = target;
            }
        }

        if (random.nextInt(100) < 70) {
            return lowestHpTarget;
        } else {
            return aliveTargets.get(random.nextInt(aliveTargets.size()));
        }
    }
}
