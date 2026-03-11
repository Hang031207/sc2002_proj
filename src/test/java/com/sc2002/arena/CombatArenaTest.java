package com.sc2002.arena;

import com.sc2002.arena.action.BasicAttack;
import com.sc2002.arena.action.Defend;
import com.sc2002.arena.action.SpecialSkill;
import com.sc2002.arena.combatant.Combatant;
import com.sc2002.arena.combatant.Goblin;
import com.sc2002.arena.combatant.Warrior;
import com.sc2002.arena.combatant.Wizard;
import com.sc2002.arena.combatant.Wolf;
import com.sc2002.arena.engine.BattleEngine;
import com.sc2002.arena.status.SmokeBombEffect;
import com.sc2002.arena.status.StunEffect;
import com.sc2002.arena.strategy.SpeedBasedTurnOrder;
import com.sc2002.arena.ui.GameUI;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Combat Arena game components.
 */
class CombatArenaTest {

    // -------------------------------------------------------------------------
    // Combatant base class
    // -------------------------------------------------------------------------

    @Test
    void warrior_takeDamage_reducesHpByDamageMinusDefense() {
        Warrior w = new Warrior("Hero");
        int hpBefore = w.getCurrentHp();
        int damage = w.takeDamage(20); // DEF = 12, so actual = 8
        assertEquals(8, damage);
        assertEquals(hpBefore - 8, w.getCurrentHp());
    }

    @Test
    void combatant_takeDamage_cannotGoBelowZero() {
        Goblin g = new Goblin("G");
        g.takeDamage(999);
        assertEquals(0, g.getCurrentHp());
        assertFalse(g.isAlive());
    }

    @Test
    void combatant_heal_cappedAtMaxHp() {
        Warrior w = new Warrior("Hero");
        w.takeDamage(30);
        w.heal(999);
        assertEquals(w.getMaxHp(), w.getCurrentHp());
    }

    // -------------------------------------------------------------------------
    // Status effects
    // -------------------------------------------------------------------------

    @Test
    void stunEffect_preventsAction_untilExpired() {
        Warrior w = new Warrior("Hero");
        w.addStatusEffect(new StunEffect(2));
        assertTrue(w.isStunned());

        w.processStatusEffects(); // tick 1
        assertTrue(w.isStunned());

        w.processStatusEffects(); // tick 2 — expires
        assertFalse(w.isStunned());
    }

    @Test
    void smokeBombEffect_expiresAfterDuration() {
        Wizard wiz = new Wizard("Mage");
        wiz.addStatusEffect(new SmokeBombEffect(1));
        assertFalse(wiz.getStatusEffects().isEmpty());

        wiz.processStatusEffects(); // tick → expire
        assertTrue(wiz.getStatusEffects().isEmpty());
    }

    @Test
    void smokeBombEffect_deterministicMissWithSeededRandom() {
        // Anonymous Random that always returns 0 → always < 50 → always miss
        java.util.Random alwaysMiss = new java.util.Random() {
            @Override public int nextInt(int bound) { return 0; }
        };
        SmokeBombEffect effect = new SmokeBombEffect(2, alwaysMiss);
        assertTrue(effect.causedMiss(), "Should miss when random returns 0");

        // Anonymous Random that always returns 99 → 99 >= 50 → never miss
        java.util.Random neverMiss = new java.util.Random() {
            @Override public int nextInt(int bound) { return 99; }
        };
        SmokeBombEffect noMissEffect = new SmokeBombEffect(2, neverMiss);
        assertFalse(noMissEffect.causedMiss(), "Should not miss when random returns 99");
    }

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    @Test
    void basicAttack_dealsDamageToTarget() {
        Warrior attacker = new Warrior("A");
        Goblin target = new Goblin("G");
        int hpBefore = target.getCurrentHp();

        new BasicAttack().execute(attacker, target);

        assertTrue(target.getCurrentHp() < hpBefore,
                "Target HP should decrease after BasicAttack");
    }

    @Test
    void defend_increasesDefenseBonus() {
        Warrior w = new Warrior("Hero");
        int defBefore = w.getDefense();
        Defend defend = new Defend();
        defend.execute(w, null);
        assertTrue(w.getDefense() > defBefore);

        defend.revert(w);
        assertEquals(defBefore, w.getDefense());
    }

    @Test
    void specialSkill_warrior_stunnsTarget() {
        Warrior warrior = new Warrior("Hero");
        Goblin goblin = new Goblin("G");

        new SpecialSkill().execute(warrior, goblin);

        assertTrue(goblin.isStunned(), "Goblin should be stunned after Warrior's Shield Bash");
    }

    @Test
    void specialSkill_wizard_appliesSmokeBombToSelf() {
        Wizard wizard = new Wizard("Mage");
        int effectsBefore = wizard.getStatusEffects().size();

        new SpecialSkill().execute(wizard, null);

        assertEquals(effectsBefore + 1, wizard.getStatusEffects().size());
        assertTrue(wizard.getStatusEffects().get(0) instanceof SmokeBombEffect);
    }

    // -------------------------------------------------------------------------
    // Turn order strategy
    // -------------------------------------------------------------------------

    @Test
    void speedBasedTurnOrder_highestSpeedActsFirst() {
        Warrior slow = new Warrior("Slow");   // speed 8
        Goblin fast = new Goblin("Fast");     // speed 16

        List<Combatant> ordered = new SpeedBasedTurnOrder()
                .orderTurns(List.of(slow, fast));

        assertEquals(fast.getName(), ordered.get(0).getName());
        assertEquals(slow.getName(), ordered.get(1).getName());
    }

    // -------------------------------------------------------------------------
    // Goblin backup spawn
    // -------------------------------------------------------------------------

    @Test
    void goblin_backupSpawn_triggersOnceBelow25Percent() {
        Goblin goblin = new Goblin("G");
        // Deal damage to go below 25 % HP threshold (HP=50, threshold=12)
        goblin.takeDamage(50); // overkill but sets to 0
        // Give it 1 HP so it's still "alive" for the check
        goblin.heal(1);

        // First check triggers
        assertTrue(goblin.checkBackupSpawn());
        // Second check should NOT trigger again
        assertFalse(goblin.checkBackupSpawn());
    }

    // -------------------------------------------------------------------------
    // Wolf getAttack override
    // -------------------------------------------------------------------------

    @Test
    void wolf_howl_increasesAttackAndReverts() {
        Wolf wolf = new Wolf("W");
        int baseAttack = wolf.getAttack();

        wolf.performSpecialSkill(null);
        assertTrue(wolf.getAttack() > baseAttack, "Howl should increase attack");

        wolf.revertHowl();
        assertEquals(baseAttack, wolf.getAttack(), "Revert should restore base attack");
    }

    // -------------------------------------------------------------------------
    // BattleEngine — win/loss detection
    // -------------------------------------------------------------------------

    @Test
    void battleEngine_detectsPlayerVictory_whenAllEnemiesDefeated() {
        Warrior player = new Warrior("Hero");
        Goblin enemy = new Goblin("G");

        // Kill the enemy before starting
        enemy.takeDamage(999);

        // Provide "1\n" as repeated input so the engine doesn't block on Scanner
        String simulatedInput = "1\n".repeat(20);
        GameUI ui = new GameUI(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));

        BattleEngine engine = new BattleEngine(List.of(player), List.of(enemy), ui);
        assertFalse(engine.isEnemyAlive());
        assertTrue(engine.isPlayerAlive());
    }

    @Test
    void battleEngine_detectsPlayerDefeat_whenAllPlayersDefeated() {
        Warrior player = new Warrior("Hero");
        player.takeDamage(999);
        Goblin enemy = new Goblin("G");

        GameUI ui = new GameUI(new Scanner(new ByteArrayInputStream("1\n".repeat(20).getBytes())));
        BattleEngine engine = new BattleEngine(List.of(player), List.of(enemy), ui);
        assertFalse(engine.isPlayerAlive());
        assertTrue(engine.isEnemyAlive());
    }
}
