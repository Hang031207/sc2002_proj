package com.sc2002.arena.status;

/**
 * Stun effect — the afflicted combatant skips their action for the duration.
 */
public class StunEffect extends StatusEffect {

    public StunEffect(int duration) {
        super("Stun", duration);
    }

    /**
     * Stun prevents action; the BattleEngine checks {@link #preventsAction()}
     * before executing a combatant's turn.
     */
    @Override
    public void onTurnStart() {
        // No additional per-turn damage; the engine simply skips the action.
    }

    /** @return {@code true} — stun always prevents the combatant from acting. */
    public boolean preventsAction() {
        return !isExpired();
    }
}
