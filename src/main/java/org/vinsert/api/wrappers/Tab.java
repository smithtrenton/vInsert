package org.vinsert.api.wrappers;

public enum Tab {

    /**
     * 47 - 53 - Top Bar
     * COMBAT, STATS, QUESTS, INVENTORY, EQUIPMEN, PRAYER, MAGIC
     * 30 - 36 - Bottom Bar
     * CLAN_CHAT, FRIENDS_LISTm IGNORE_LIST, LOGOUT, OPTIONS, EMOTES, MUSIC
     * texture | 861580522 ( OPEN )
     * texture | -1347704871 ( CLOSED )
     */

    COMBAT(47),
    STATS(48),
    QUESTS(49),
    INVENTORY(50),
    EQUIPMENT(51),
    PRAYER(52),
    MAGIC(53),
    CLAN_CHAT(30),
    FRIENDS_LIST(31),
    IGNORE_LIST(32),
    LOGOUT(33),
    OPTIONS(34),
    EMOTES(35),
    MUSIC(36);

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
