package org.vinsert.api.wrappers;

/**
 * @author const_
 */
public enum Skill {

    ATTACK(0),
    STRENGTH(2),
    DEFENCE(1),
    RANGED(4),
    PRAYER(5),
    MAGIC(6),
    RUNECRAFT(20),
    HITPOINTS(3),
    AGILITY(16),
    HERBLORE(15),
    THIEVING(17),
    CRAFTING(12),
    FLETCHING(9),
    SLAYER(18),
    MINING(14),
    SMITHING(13),
    FISHING(10),
    COOKING(7),
    FIREMAKING(11),
    WOODCUTTING(8),
    FARMING(19),
    HUNTER(21),
    OVERALL(-1),
    COMBAT(-1);

    private final int index;

    private Skill(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}
