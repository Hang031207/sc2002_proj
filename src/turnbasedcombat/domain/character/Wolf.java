package turnbasedcombat.domain.character;

import turnbasedcombat.control.BattleEngine;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.domain.action.BasicAttack;

public class Wolf extends Combatant {

    public Wolf(String name) {
        super(name, 40, 45, 5, 35);
    }

    public Wolf() {
        this("Wolf");
    }

    @Override
    public void takeTurn(BattleEngine engine) {
        // Wolf's turn logic
        if (!this.isAlive()) return; // Skip turn if dead
        Combatant players = engine.getPlayerTeam();
        Action basicAttack = new BasicAttack();
        basicAttack.execute(this, players);
    }

}
