package com.sc2002.arena;

import com.sc2002.arena.combatant.Combatant;
import com.sc2002.arena.combatant.Goblin;
import com.sc2002.arena.combatant.Warrior;
import com.sc2002.arena.combatant.Wizard;
import com.sc2002.arena.combatant.Wolf;
import com.sc2002.arena.engine.BattleEngine;
import com.sc2002.arena.ui.GameUI;

import java.util.List;

/**
 * Entry point for the Combat Arena game.
 *
 * <p>Responsibilities here are intentionally minimal — just wiring up the
 * objects selected by the player and delegating to {@link BattleEngine}.
 */
public class Main {

    public static void main(String[] args) {
        GameUI ui = new GameUI();

        String playerName = ui.promptPlayerName();
        int classChoice  = ui.promptCharacterChoice();

        Combatant player = (classChoice == 2)
                ? new Wizard(playerName)
                : new Warrior(playerName);

        List<Combatant> players = List.of(player);
        List<Combatant> enemies = List.of(new Goblin("Goblin"), new Wolf("Alpha Wolf"));

        BattleEngine engine = new BattleEngine(players, enemies, ui);
        engine.start();
    }
}
