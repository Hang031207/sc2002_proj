package turnbasedcombat.domain.character;

public class Goblin extends Combatant {
    private static final int SNEAKY_STAB_MANA_COST = 10;

    public Goblin(String name) {
        super(name, 50, 12, 4, 8, 20);
    }

    public Goblin() {
        this("Goblin");
    }

    @Override
    public String getSpecialSkillName() {
        return "Sneaky Stab";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "A quick stab that ignores some defense.";
    }

    @Override
    public int getSpecialSkillManaCost() {
        return SNEAKY_STAB_MANA_COST;
    }
}
