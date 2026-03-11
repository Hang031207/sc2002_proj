package com.sc2002.arena.action;

import com.sc2002.arena.combatant.Combatant;

/**
 * Special Skill — delegates to the combatant's own
 * {@link Combatant#performSpecialSkill(Combatant)} implementation.
 *
 * <p>This adapter bridges the {@link Action} Command interface and the
 * Template Method defined in {@link Combatant}, keeping both patterns intact
 * without duplication.
 */
public class SpecialSkill implements Action {

    @Override
    public void execute(Combatant actor, Combatant target) {
        actor.performSpecialSkill(target);
    }

    @Override
    public String getName() {
        return "Special Skill";
    }

    @Override
    public String getDescription() {
        return "Uses this combatant's unique special skill.";
    }
}
