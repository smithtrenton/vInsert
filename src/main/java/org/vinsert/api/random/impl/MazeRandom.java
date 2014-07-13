package org.vinsert.api.random.impl;

import org.vinsert.api.collection.Filter;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Tile;
import org.vinsert.api.wrappers.interaction.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : const_
 */
@LoginRequired
@RandomManifest(name = "Maze Solver", author = "const_", version = 0.1)
public final class MazeRandom extends RandomSolver {

    private final ArrayList<Tile> wrongDoors = new ArrayList<>();
    private ArrayList<Tile> alreadyUsedDoors = new ArrayList<>();

    public static final int OPEN_DOOR_ID = 83;
    public static final String FINAL_CHEST = "Strange shrine";
    private GameObject currentDoor = null;

    @Override
    public boolean canRun() {
        if (widgets.find().text("Complete the Maze as fast as possible").visible().exists()) {
            return true;
        }
        currentDoor = null;
        wrongDoors.clear();
        alreadyUsedDoors.clear();
        return false;
    }

    @Override
    public int run() {
      /*  if(true) {
            tabs.logout();
            session.getScriptManager().stop();
        }*/
        if (camera.getPitch() < 80) {
            camera.setPitch(Utilities.random(80, 90));
        }
        if (currentDoor == null) {
            final GameObject chest = objects.find(FINAL_CHEST).canReach().single();
            if (chest != null) {
                if (chest.distanceTo(player) < 2) {
                    if (chest.interact("Touch") == Result.OK) {
                        Utilities.sleep(800, 2000);
                        return 400;
                    }
                }
            }
            currentDoor = getNextDoor();
            if (currentDoor == null) {
                if (alreadyUsedDoors != null) {
                    if (alreadyUsedDoors.size() > 0) {
                        for (int i = 0; i < alreadyUsedDoors.size(); i++) {
                            if (alreadyUsedDoors.get(i) != null) {
                                alreadyUsedDoors.remove(i);
                            }
                        }
                    }
                }
            }
        } else {
            if (notPassable()) {
                wrongDoors.add(currentDoor.getTile());
                currentDoor = null;
                if (widgets.canContinue()) {
                    widgets.clickContinue();
                    return 200;
                }
            } else if (objects.find(OPEN_DOOR_ID).single() != null) {
                int i = 0;
                while (i < 30 && objects.find(OPEN_DOOR_ID).single() != null) {
                    i++;
                    Utilities.sleep(40, 60);
                }
                alreadyUsedDoors.add(currentDoor.getTile());
                currentDoor = null;
            } else if (widgets.canContinue()) {
                widgets.clickContinue();
                return 200;
            } else {
                if (currentDoor == null) {
                    currentDoor = getNextDoor();
                }
                if (!players.getLocal().isMoving() && players.getLocal().getAnimationId() == -1) {
                    if (currentDoor.distanceTo(player) > 4) {
                        walking.walk(currentDoor);
                        for (int i = 0; i < 20 && !players.getLocal().isMoving(); i++, Utilities.sleep(
                                40, 60))
                            ;
                        for (int i = 0; i < 50 && players.getLocal().isMoving(); i++, Utilities.sleep(
                                50, 150))
                            ;
                    } else {
                        turnCameraToDoor();
                        if (currentDoor.interact("Open") == Result.OK) {
                            for (int i = 0; i < 40 && objects.find(OPEN_DOOR_ID).single() == null; i++, Utilities
                                    .sleep(70, 130))
                                ;
                            if (objects.find(OPEN_DOOR_ID).single() != null) {
                                for (int i = 0; i < 50
                                        && objects.find(OPEN_DOOR_ID).single() != null; i++, Utilities
                                        .sleep(100, 150))
                                    ;
                                alreadyUsedDoors.add(currentDoor.getTile());
                                currentDoor = null;
                            }
                        }
                    }
                }
            }
        }
        return Utilities.random(80, 130);
    }

    private void turnCameraToDoor() {
        final GameObject border = objects.find().filter(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                return acceptable.getModel() != null && acceptable.getModel().getPolygons() != null &&
                        acceptable.getModel().getPolygons().length > 100 && acceptable.getModel().getPolygons().length < 250;
            }
        }).single();
        if (border != null) {
            final Tile borderTile = border.getTile();
            final Tile playerTile = player.getTile();
            if (Math.abs(borderTile.getX() - playerTile.getX()) > Math.abs(borderTile
                    .getY() - playerTile.getY())) {
                if (borderTile.getX() > playerTile.getX()) {
                    camera.setAngle(Utilities.random(260, 280));
                } else {
                    camera.setAngle(Utilities.random(80, 100));
                }
            } else {
                if (borderTile.getY() > playerTile.getY()) {
                    camera.setAngle(Utilities.random(170, 190));
                } else {
                    int i = Utilities.random(-10, 10);
                    if (i < 0) {
                        i += 360;
                    }
                    if (i >= 360) {
                        i -= 360;
                    }
                    camera.setAngle(i);
                }
            }
        }
    }

    private boolean notPassable() {
        return widgets.find(210, 0).text("don't think that's the").visible().exists();
    }

    private GameObject getNextDoor() {
        final ArrayList<Tile> walkableTiles = getReachableAllTiles(player.getTile());
        for (final Tile t : walkableTiles) {
            if (t != null) {
                for (GameObject obj : objects.find().filter(new Filter<GameObject>() {

                    @Override
                    public boolean accept(GameObject o) {
                        if (o != null) {
                            return walking.canReach(o.getTile(), player.getTile()) ||
                                    walking.canReach(new Tile(o.getX() + 1, o.getY()), player.getTile()) ||
                                    walking.canReach(new Tile(o.getX() - 1, o.getY()), player.getTile()) ||
                                    walking.canReach(new Tile(o.getX(), o.getY() + 1), player.getTile()) ||
                                    walking.canReach(new Tile(o.getX(), o.getY() - 1), player.getTile());
                        }
                        return false;
                    }

                })) {
                    if (isRightDoor(obj)) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    private boolean isRightDoor(GameObject o) {
        if (o != null) {
            if (o.getModel() != null && o.getModel().getPolygons() != null && o.getModel().getPolygons().length > 300) {
                if (!wrongDoors.contains(o.getTile())
                        && !alreadyUsedDoors.contains(o.getTile())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Tile> getReachableAllTiles(final Tile start) {
        final ArrayList<Tile> result = new ArrayList<>();
        int j = 0;
        int turned = 0;
        Tile last = null;
        final GameObject border = objects.find().filter(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject acceptable) {
                return acceptable.getModel() != null && acceptable.getModel().getPolygons() != null &&
                        acceptable.getModel().getPolygons().length > 100 && acceptable.getModel().getPolygons().length < 250;
            }
        }).single();
        if (!isWalkable(start)) {
            if (border != null) {
                int index = 0;
                final ArrayList<Tile> cache = getNearestWalkableTiles(start);
                if (cache != null && cache.size() > 0) {
                    for (int i = 0; i < cache.size(); i++) {
                        if (Math.sqrt(Math.pow(
                                Math.abs(cache.get(i).getX() - border.getTile().getX()), 2)
                                + Math.pow(
                                Math.abs(cache.get(i).getY() - border.getTile().getY()),
                                2)) < Math.sqrt(Math.pow(
                                Math.abs(cache.get(index).getX() - border.getTile().getX()), 2)
                                + Math.pow(
                                Math.abs(cache.get(index).getY()
                                        - border.getTile().getY()), 2
                        ))) {
                            index = i;
                        }
                    }
                }
                last = cache.get(index);
            }
        } else {
            last = start;
        }
        if (last == null) {
            return null;
        }
        Tile current = getNearestWalkableTiles(last).get(0);

        while (j < 100 && turned < 3) {
            j++;
            ArrayList<Tile> tiles = getNearestWalkableTiles(current);
            if (tiles.size() > 1) {
                for (Tile tile : tiles) {
                    if (tile.getX() != last.getX() || tile.getY() != last.getY()) {
                        if (!result.contains(current)) {
                            result.add(current);
                        }
                        last = current;
                        current = tile;
                        break;
                    }
                }
            } else {
                turned++;
                if (!result.contains(current)) {
                    result.add(current);
                }
                last = current;
                current = tiles.get(0);
            }
        }
        return result;
    }

    private ArrayList<Tile> getNearestWalkableTiles(final Tile tile) {
        final ArrayList<Tile> tiles = new ArrayList<Tile>();
        if (isWalkable(new Tile(tile.getX() + 1, tile.getY()))) {
            tiles.add(new Tile(tile.getX() + 1, tile.getY()));
        }
        if (isWalkable(new Tile(tile.getX() - 1, tile.getY()))) {
            tiles.add(new Tile(tile.getX() - 1, tile.getY()));
        }
        if (isWalkable(new Tile(tile.getX(), tile.getY() + 1))) {
            tiles.add(new Tile(tile.getX(), tile.getY() + 1));
        }
        if (isWalkable(new Tile(tile.getX(), tile.getY() - 1))) {
            tiles.add(new Tile(tile.getX(), tile.getY() - 1));
        }
        return tiles;

    }

    private boolean isWalkable(final Tile t) {
        List<GameObject> objs = objects.find().filter(new Filter<GameObject>() {

            @Override
            public boolean accept(GameObject arg0) {
                return arg0.getTile().equals(t);
            }

        }).asList();
        if (objs != null) {
            for (GameObject ob : objs) {
                if (ob != null) {
                    if (ob.getType() == GameObject.GameObjectType.WALL) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}