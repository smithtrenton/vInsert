package org.vinsert.api.wrappers;

/**
 * Wrapper for a tile on the RuneScape map
 */
public final class Tile {

    private int x;
    private int y;
    private int z;

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tile(int x, int y) {
        this(x, y, 0);
    }

    public Tile() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "X: " + x +
                ", Y: " + y +
                ", Z: " + z;
    }

    /**
     * Gets the distance to another tile
     *
     * @param tile The tile to find the distance to
     * @return The distance between this and the tile specified
     */
    public int distanceTo(Tile tile) {
        int px = tile.getX();
        int py = tile.getY();
        px -= getX();
        py -= getY();
        return (int) Math.sqrt(px * px + py * py);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tile)) {
            return super.equals(object);
        }
        Tile tile = (Tile) object;
        return tile.x == x && tile.y == y;
    }

    @Override
    public int hashCode() {
        return x >> 4 + y << 5 + z;
    }

}
