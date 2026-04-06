package turnbasedcombat.domain.character;

import turnbasedcombat.domain.action.Action;
import turnbasedcombat.control.BattleEngine;
import java.util.List;

public class Wizard extends Combatant {
    public Wizard(String name) {
        super(name, 200, 50, 10, 20); 
    }

    @Override
    public void executeSpecialSkill(Combatant target, List<Combatant> allCombatants) {
        for (Combatant enemy : allCombatants) {
            if ((enemy instanceof Goblin || enemy instanceof Wolf) && enemy.isAlive()) {
                enemy.takeDamage(this.attack);
                if (!enemy.isAlive()) {
                    this.modifyAttack(10);
                    System.out.println(this.name + " gained +10 ATK from defeating " + enemy.getName() + "!");
                }
            }
        }
    }

    @Override
    public void takeTurn(BattleEngine engine) {
        Action action = engine.getCli().getPlayerAction(this);
        Combatant c_target = engine.getCli().getPlayerTarget(this, action, engine.getEnemyTeam(), engine.getPlayerTeam());
        
        if (c_target != null && action.canExecute(this)) {
            engine.getCli().displayActionExecution(this, action, c_target);
            action.execute(this, c_target, engine.getAllCombatants());
            engine.getCli().displayActionResult(this, action, c_target);
        }
    }
}