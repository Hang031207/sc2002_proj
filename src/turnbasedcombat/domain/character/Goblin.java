package turnbasedcombat.domain.character;

import turnbasedcombat.control.BattleEngine;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.domain.action.BasicAttack;
import turnbasedcombat.domain.strategy;

public class Goblin extends Combatant {

    public Goblin(String name) {
        super(name, 50,35, 15, 25);
    }

    public Goblin() {
        this("Goblin");
    }

    @Override
    public void takeTurn(BattleEngine engine) {
        // Goblin's turn logic
        if (!this.isAlive()) return; // Skip turn if dead
        Combatant players = engine.getPlayerTeam();
        Action basicAttack = new BasicAttack();
        basicAttack.execute(this, players);
    }
}
