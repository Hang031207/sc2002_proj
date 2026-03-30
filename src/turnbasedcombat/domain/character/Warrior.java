package turnbasedcombat.domain.character;

public class Warrior extends Combatant {
    private static final int SHIELD_BASH_MANA_COST = 15;

    public Warrior(String name) {
        super(name, 120, 18, 12, 5, 30);
    }

    public Warrior() {
        this("Warrior");
    }

    @Override
    public String getSpecialSkillName() {
        return "Shield Bash";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "A powerful bash that deals damage and may stun the target.";
    }

    @Override
    public int getSpecialSkillManaCost() {
        return SHIELD_BASH_MANA_COST;
    }
}
