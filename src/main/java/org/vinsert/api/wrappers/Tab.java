package org.vinsert.api.wrappers;

public enum Tab {

    /**
     * 48 - 54 - Top Bar
     * COMBAT, STATS, QUESTS, INVENTORY, EQUIPMEN, PRAYER, MAGIC
     * 31 - 37 - Bottom Bar
     * CLAN_CHAT, FRIENDS_LISTm IGNORE_LIST, LOGOUT, OPTIONS, EMOTES, MUSIC
     * texture | 861580522 ( OPEN )
     * texture | -1347704871 ( CLOSED )
     */

    COMBAT(48),
    STATS(49),
    QUESTS(50),
    INVENTORY(51),
    EQUIPMENT(52),
    PRAYER(53),
    MAGIC(54),
    CLAN_CHAT(31),
    FRIENDS_LIST(32),
    IGNORE_LIST(33),
    LOGOUT(34),
    OPTIONS(35),
    EMOTES(36),
    MUSIC(37);

    private final int id;

    private Tab(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Tab byId(int id) {
        for (Tab t : values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}
