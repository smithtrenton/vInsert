package org.vinsert.game.engine.cache.media;

/**
 * @author tobiewarburton
 */
public interface IModel {

    int[] getVerticesX();

    int[] getVerticesY();

    int[] getVerticesZ();

    int[] getTrianglesX();

    int[] getTrianglesY();

    int[] getTrianglesZ();

    int[] getTexturedTrianglesX();

    int[] getTexturedTrianglesY();

    int[] getTexturedTrianglesZ();

    IModel model(IModel model, int i);

    boolean canRender();

    void setRender(boolean on);

}
