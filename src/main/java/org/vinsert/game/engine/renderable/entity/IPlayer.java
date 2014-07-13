package org.vinsert.game.engine.renderable.entity;

import org.vinsert.game.engine.cache.composite.IPlayerComposite;
import org.vinsert.game.engine.net.IBuffer;

/**
 * @author tobiewarburton
 */
public interface IPlayer extends IEntity {


    String getName();


    IPlayerComposite getComposite();


    int getPrayerIcon();

    int getSkullIcon();


    int getTeam();


    int getGender();


    int getCombatLevel();


    void updatePlayer(IBuffer buffer);

}
