package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public interface StatusEffect {
    void applyEffect(Combatant target);
    void removeEffect(Combatant target);
    void decrementDuration();
    int getRemainingDuration();
    boolean isExpired();
    String getName();
}
