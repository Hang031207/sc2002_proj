package turnbasedcombat.domain.character;

public class Wizard extends Combatant {
    private static final int ARCANE_BLAST_MANA_COST = 25;

    public Wizard(String name) {
        super(name, 80, 25, 5, 7, 100);
    }

    public Wizard() {
        this("Wizard");
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast";
    }

    @Override
    public String getSpecialSkillDescription() {
        return "A powerful magical attack that deals heavy damage and boosts next attack.";
    }

    @Override
    public int getSpecialSkillManaCost() {
        return ARCANE_BLAST_MANA_COST;
    }
}
