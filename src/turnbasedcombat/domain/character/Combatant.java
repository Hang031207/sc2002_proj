package turnbasedcombat.domain.character;

import turnbasedcombat.domain.effect.StatusEffect;
import turnbasedcombat.domain.item.Item;
import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int speed;
    protected List<StatusEffect> statusEffects;
    protected List<Item> inventory;
    protected boolean isDefending;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.statusEffects = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.isDefending = false;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean isDefending() {
        return isDefending;
    }

    public void setDefending(boolean defending) {
        this.isDefending = defending;
    }

    public void takeDamage(int damage) {
        int actualDamage = damage;
        if (isDefending) {
            actualDamage = damage / 2;
        }
        actualDamage = Math.max(0, actualDamage - defense);
        this.hp = Math.max(0, this.hp - actualDamage);
    }

    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
        effect.apply(this);
    }

    public void removeStatusEffect(StatusEffect effect) {
        effect.remove(this);
        statusEffects.remove(effect);
    }

    public List<StatusEffect> getStatusEffects() {
        return new ArrayList<>(statusEffects);
    }

    public void tickStatusEffects() {
        List<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffect effect : statusEffects) {
            effect.tick(this);
            if (effect.isExpired()) {
                toRemove.add(effect);
            }
        }
        for (StatusEffect effect : toRemove) {
            removeStatusEffect(effect);
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void modifyAttack(int amount) {
        this.attack += amount;
    }

    public void modifyDefense(int amount) {
        this.defense += amount;
    }

    public void modifySpeed(int amount) {
        this.speed += amount;
    }

    public abstract String getSpecialSkillName();
    public abstract String getSpecialSkillDescription();
    public abstract void executeSpecialSkill(Combatant target, List<Combatant> allCombatants);
}
