package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class SmokeBombEffect implements StatusEffect {
    private int duration;
    private final String name = "Smoke Screen";

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        
    }

    @Override
    public void remove(Combatant target) {
     
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

    @Override
    public int modifyIncomingDamage(int damage) {
        return 0;
    }
}
