package turnbasedcombat.domain.character;

public class Wolf extends Combatant {
    private static final int FEROCIOUS_BITE_MANA_COST = 8;

    public Wolf(String name) {
        super(name, 60, 15, 3, 10, 15);
    }

    public Wolf() {
        this("Wolf");
    }

    @Override
    public String getSpecialSkillName() {
        return "Ferocious Bite";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "A savage bite that deals extra damage to wounded targets.";
    }

    @Override
    public int getSpecialSkillManaCost() {
        return FEROCIOUS_BITE_MANA_COST;
    }
}
