package org.vinsert.game.engine;

import org.vinsert.api.event.Events;
import org.vinsert.core.Environment;
import org.vinsert.game.engine.collection.IBag;
import org.vinsert.game.engine.collection.ICache;
import org.vinsert.game.engine.collection.IDeque;
import org.vinsert.game.engine.input.IKeyboard;
import org.vinsert.game.engine.input.IMouse;
import org.vinsert.game.engine.media.IWidget;
import org.vinsert.game.engine.net.IBufferedConnection;
import org.vinsert.game.engine.net.IPacketBuffer;
import org.vinsert.game.engine.renderable.entity.INpc;
import org.vinsert.game.engine.renderable.entity.IPlayer;
import org.vinsert.game.engine.scene.ICollisionMap;
import org.vinsert.game.engine.scene.IScene;

import java.awt.*;

/**
 * @author const_
 */
public interface IClient {

    void setEnvironment(Environment env);

    Environment getEnvironment();

    Events getEventBus();

    void setEventBus(Events events);

    IPacketBuffer createBuffer();


    ICache getNpcCompositeCache();


    ICache getObjectCompositeCache();


    ICache getItemCompositeCache();


    int getCurrentDrawX();


    int getCurrentDrawY();


    int[] getSineTable();


    int[] getCosineTable();


    IKeyboard getKeyboard();


    IMouse getMouse();


    int getPlayerRights();


    ICollisionMap[] getMaps();


    IDeque getProjectiles();


    IDeque[][][] getLoot();


    IPlayer[] getPlayers();


    IPlayer getLocalPlayer();


    int getCameraX();


    int getCameraY();


    int getCameraZ();


    int getCameraPitch();


    int getCameraYaw();


    int getDestinationX();


    int getDestinationY();


    int getPlane();


    INpc[] getNpcs();


    int getBaseX();


    int getBaseY();


    int[] getSkillLevels();


    int[] getSkillBases();


    int[] getSkillExperiences();


    boolean isMenuOpen();


    int getMenuCount();

    int getCursorCrosshair();


    String[] getMenuActions();


    String[] getMenuOptions();


    int getMenuX();


    int getMenuY();


    int getMenuWidth();


    int getMenuHeight();


    int getLoopCycle();


    IScene getScene();


    byte[][][] getTileSettings();


    int[][][] getTileHeights();


    int getMinimapAngle();


    int getMinimapScale();


    int getMinimapOffset();


    IBag getWidgetNodeBag();


    Canvas getCanvas();


    int getLoginIndex();


    int[] getWidgetBoundsX();


    int[] getWidgetBoundsY();


    int[] getWidgetBoundsWidth();


    int[] getWidgetBoundsHeight();


    int[] getWidgetSettings();


    int[] getPlayerSettings();


    void addListeners();


    void updateWidgetNodes();


    int getTileHeight(int x, int y, int plane);


    void worldToScreen(int i, int i2, int i3);


    void updateLocalPlayerPosition(boolean bool);


    void updateLoot();


    void js5Method();


    IWidget[][] getWidgets();

    ICache getNpcModelCache();

    IBufferedConnection getConnection();

    IPacketBuffer getPacketBuffer();
}
