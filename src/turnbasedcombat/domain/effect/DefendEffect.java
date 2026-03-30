package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class DefendEffect implements StatusEffect {
    private int duration;
    private final String name = "Defending";

    public DefendEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        target.setDefending(true);
    }

    @Override
    public void remove(Combatant target) {
        target.setDefending(false);
    }

    @Override
    public void tick(Combatant target) {
        duration--;
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRemainingDuration() {
        return duration;
    }
}
