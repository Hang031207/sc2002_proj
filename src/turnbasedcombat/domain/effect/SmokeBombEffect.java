package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class SmokeBombEffect implements StatusEffect {
    private int duration;
    private final int evasionBoost;
    private final String name = "Smoke Screen";

    public SmokeBombEffect(int duration, int evasionBoost) {
        this.duration = duration;
        this.evasionBoost = evasionBoost;
    }

    @Override
    public void apply(Combatant target) {
        target.modifySpeed(evasionBoost);
    }

    @Override
    public void remove(Combatant target) {
        target.modifySpeed(-evasionBoost);
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

    public int getEvasionBoost() {
        return evasionBoost;
    }
}
