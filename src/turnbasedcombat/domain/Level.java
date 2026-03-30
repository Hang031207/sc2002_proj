package turnbasedcombat.domain;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.character.Goblin;
import turnbasedcombat.domain.character.Wolf;
import turnbasedcombat.domain.strategy.EnemyStrategy;
import turnbasedcombat.domain.strategy.BasicAttackStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Level {
    private final int levelNumber;
    private final String levelName;
    private final List<Combatant> enemies;
    private final Map<Combatant, EnemyStrategy> enemyStrategies;

    public Level(int levelNumber, String levelName) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.enemies = new ArrayList<>();
        this.enemyStrategies = new HashMap<>();
    }

    public void addEnemy(Combatant enemy, EnemyStrategy strategy) {
        enemies.add(enemy);
        enemyStrategies.put(enemy, strategy);
    }

    public List<Combatant> getEnemies() {
        return new ArrayList<>(enemies);
    }

    public Map<Combatant, EnemyStrategy> getEnemyStrategies() {
        return new HashMap<>(enemyStrategies);
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLevelName() {
        return levelName;
    }

    public static Level createLevel1() {
        Level level = new Level(1, "Forest Clearing");
        EnemyStrategy basicStrategy = new BasicAttackStrategy();

        Goblin goblin1 = new Goblin("Goblin Scout");
        Goblin goblin2 = new Goblin("Goblin Grunt");

        level.addEnemy(goblin1, basicStrategy);
        level.addEnemy(goblin2, basicStrategy);

        return level;
    }

    public static Level createLevel2() {
        Level level = new Level(2, "Dark Woods");
        EnemyStrategy basicStrategy = new BasicAttackStrategy();

        Wolf wolf1 = new Wolf("Dire Wolf");
        Wolf wolf2 = new Wolf("Shadow Wolf");
        Goblin goblin = new Goblin("Goblin Shaman");

        level.addEnemy(wolf1, basicStrategy);
        level.addEnemy(wolf2, basicStrategy);
        level.addEnemy(goblin, basicStrategy);

        return level;
    }

    public static Level createLevel3() {
        Level level = new Level(3, "Goblin Stronghold");
        EnemyStrategy basicStrategy = new BasicAttackStrategy();

        Goblin goblinChief = new Goblin("Goblin Chief");
        goblinChief.modifyAttack(5);
        goblinChief.heal(30);

        Wolf alphaWolf = new Wolf("Alpha Wolf");
        alphaWolf.modifyAttack(3);
        alphaWolf.heal(20);

        Goblin guard1 = new Goblin("Goblin Guard");
        Goblin guard2 = new Goblin("Goblin Guard");

        level.addEnemy(goblinChief, basicStrategy);
        level.addEnemy(alphaWolf, basicStrategy);
        level.addEnemy(guard1, basicStrategy);
        level.addEnemy(guard2, basicStrategy);

        return level;
    }
}
