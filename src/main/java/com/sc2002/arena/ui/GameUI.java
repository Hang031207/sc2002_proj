package com.sc2002.arena.ui;

import com.sc2002.arena.action.Action;
import com.sc2002.arena.combatant.Combatant;
import com.sc2002.arena.engine.ActionResult;

import java.util.List;
import java.util.Scanner;

/**
 * GameUI — Boundary class responsible for ALL console I/O.
 *
 * <p>The BattleEngine never calls {@code System.out} or {@code Scanner} directly;
 * it delegates every display and input concern to this class, keeping the engine
 * logic pure and easily testable.
 *
 * <p>To support automated testing or alternative front-ends, this class can be
 * subclassed or replaced with a stub.
 */
public class GameUI {

    private final Scanner scanner;

    public GameUI() {
        this.scanner = new Scanner(System.in);
    }

    /** Constructor for testing — allows injecting a custom input source. */
    public GameUI(Scanner scanner) {
        this.scanner = scanner;
    }

    // -------------------------------------------------------------------------
    // Battle start / end
    // -------------------------------------------------------------------------

    public void displayBattleStart(List<Combatant> players, List<Combatant> enemies) {
        printSeparator('=', 60);
        System.out.println("           ⚔  COMBAT ARENA  ⚔");
        printSeparator('=', 60);
        System.out.println("PLAYERS:");
        players.forEach(p -> System.out.println("  " + p.getStatusLine()));
        System.out.println("ENEMIES:");
        enemies.forEach(e -> System.out.println("  " + e.getStatusLine()));
        printSeparator('-', 60);
    }

    public void displayBattleEnd(List<Combatant> winners, boolean playerWon) {
        printSeparator('=', 60);
        if (playerWon) {
            System.out.println("  🏆  VICTORY! The players have won the battle!");
        } else {
            System.out.println("  💀  DEFEAT! All heroes have fallen.");
        }
        System.out.println("Survivors:");
        winners.forEach(w -> System.out.println("  " + w.getStatusLine()));
        printSeparator('=', 60);
    }

    // -------------------------------------------------------------------------
    // Round display
    // -------------------------------------------------------------------------

    public void displayRoundStart(int roundNumber) {
        printSeparator('-', 60);
        System.out.printf("  Round %d%n", roundNumber);
        printSeparator('-', 60);
    }

    public void displayRoundEnd(int roundNumber, List<Combatant> players, List<Combatant> enemies) {
        System.out.println("-- End of Round " + roundNumber + " --");
        System.out.println("Players:");
        players.stream().filter(Combatant::isAlive)
               .forEach(p -> System.out.println("  " + p.getStatusLine()));
        System.out.println("Enemies:");
        enemies.stream().filter(Combatant::isAlive)
               .forEach(e -> System.out.println("  " + e.getStatusLine()));
    }

    // -------------------------------------------------------------------------
    // Action display
    // -------------------------------------------------------------------------

    public void displayActionResult(ActionResult result) {
        System.out.println("  > " + result.getMessage());
    }

    public void displayMessage(String message) {
        System.out.println("  [!] " + message);
    }

    // -------------------------------------------------------------------------
    // Player input
    // -------------------------------------------------------------------------

    /**
     * Prompts the player to choose an action from the provided list.
     *
     * @param actor   the combatant whose turn it is
     * @param actions the available actions
     * @return 1-based index of the chosen action (defaults to 1 on invalid input)
     */
    public int promptActionChoice(Combatant actor, List<Action> actions) {
        System.out.println("\n" + actor.getName() + "'s turn:");
        System.out.println("  Status effects: " + actor.getStatusEffects());
        System.out.println("  Choose an action:");
        for (int i = 0; i < actions.size(); i++) {
            Action a = actions.get(i);
            System.out.printf("    %d. %s — %s%n", i + 1, a.getName(), a.getDescription());
        }
        System.out.print("  Enter choice (1-" + actions.size() + "): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= actions.size()) {
                return choice;
            }
        } catch (NumberFormatException ignored) {
            // fall through to default
        }

        System.out.println("  Invalid input — defaulting to Basic Attack.");
        return 1;
    }

    /**
     * Prompts the player to choose their character class.
     *
     * @return 1 for Warrior, 2 for Wizard
     */
    public int promptCharacterChoice() {
        System.out.println("Choose your character:");
        System.out.println("  1. Warrior — HP:120  ATK:18  DEF:12  SPD:8");
        System.out.println("  2. Wizard  — HP:80   ATK:24  DEF:5   SPD:14");
        System.out.print("  Enter choice (1-2): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 1 || choice == 2) return choice;
        } catch (NumberFormatException ignored) {
            // fall through to default
        }

        System.out.println("  Invalid input — defaulting to Warrior.");
        return 1;
    }

    /**
     * Prompts the player to enter their character's name.
     *
     * @return entered name (non-blank), or "Hero" as fallback
     */
    public String promptPlayerName() {
        System.out.print("Enter your hero's name: ");
        String name = scanner.nextLine().trim();
        return name.isEmpty() ? "Hero" : name;
    }

    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------

    /** Closes the underlying {@link Scanner}. Should be called when the game exits. */
    public void close() {
        scanner.close();
    }

    private void printSeparator(char ch, int length) {
        System.out.println(String.valueOf(ch).repeat(length));
    }
}
