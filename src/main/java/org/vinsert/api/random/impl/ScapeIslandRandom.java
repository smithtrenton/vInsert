package org.vinsert.api.random.impl;

import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.*;
import org.vinsert.api.wrappers.interaction.Result;

import java.util.List;

/**
 * @author Hexagon
 *         <p/>
 *         13/04/2014
 */

@RandomManifest(name = "ScapeIsland Random", version = 0.1)
public final class ScapeIslandRandom extends RandomSolver {
    private static final int WIDGET_LOOKING_STATUE = 186;
    private static final int FISHING_NET = 6209;
    private Orientation fishSpotOrientation;
    private GameObject fishingSpot;
    private boolean canLeaveFlag = false;

    private boolean isLooking() {
        return widgets.find(WIDGET_LOOKING_STATUE, 0).exists();
    }

    private Orientation getCameraOrientation(int angle) {
        if ((angle >= 0 && angle < 30) || (angle > 330 && angle <= 360)) {
            return Orientation.NORTH;
        } else if (angle > 60 && angle < 120) {
            return Orientation.WEST;
        } else if (angle > 160 && angle < 210) {
            return Orientation.SOUTH;
        } else if (angle > 240 && angle < 300) {
            return Orientation.EAST;
        }
        return null;
    }

    private Orientation getOrientationTo(Tile tile) {
        final Tile base = npcs.find("Servant").single().getTile();
        final int tolerance = 3;
        if (tile.getY() >= base.getY() && base.getX() < tile.getX() + tolerance
                && base.getX() > tile.getX() - tolerance) {
            return Orientation.NORTH;
        } else if (tile.getY() <= base.getY() && base.getX() < tile.getX() + tolerance
                && base.getX() > tile.getX() - tolerance) {
            return Orientation.SOUTH;
        } else if (tile.getX() >= base.getX() && base.getY() < tile.getY() + tolerance
                && base.getY() > tile.getY() - tolerance) {
            return Orientation.EAST;
        } else if (tile.getX() <= base.getX() && base.getY() < tile.getY() + tolerance
                && base.getY() > tile.getY() - tolerance) {
            return Orientation.WEST;
        }
        return null;
    }

    private GameObject getFishingSpot() {
        List<GameObject> fishSpots = objects.find("Fishing spot").asList();
        for (GameObject obj : fishSpots) {
            if (getOrientationTo(obj.getTile()) == fishSpotOrientation) {
                return obj;
            }
        }
        return null;
    }

    private void log(Object obj) {
        System.out.println("[ScapeIslandSolver] " + obj);
    }

    @Override
    public boolean canRun() {
        if (!npcs.find("Evil Bob").exists() || !npcs.find("Servant").exists()) {
            canLeaveFlag = false;
            fishingSpot = null;
            fishSpotOrientation = null;
            return false;
        }
        return true;
    }

    @Override
    public int run() {
        log("-----------------------------------------------------");
        if (fishSpotOrientation == null) {
            log("Finding orientation");
            if (!isLooking()) {
                log("Not looking to spot, talking to servant");
                if (widgets.canContinue()) {
                    if (widgets.clickContinue() == Result.OK) {
                        return Utilities.random(800, 1200);
                    }
                } else {
                    Npc servant = npcs.find("Servant").single();
                    if (servant != null) {
                        switch (servant.interact("Talk-to")) {
                            case NOT_ON_SCREEN:
                                walking.walk(servant);
                                break;
                            case OK:
                                return Utilities.random(800, 1200);
                            default:
                                break;
                        }
                    } else {
                        log("Unable to find servant!");
                    }
                }
            } else {
                log("We are looking and grabbing orientation");
                fishSpotOrientation = getCameraOrientation(camera.getAngle());
            }
        } else if (!inventory.contains(FISHING_NET) && !isLooking()) {
            log("We are grabbing a fishing net");
            Loot fishNet = loot.find().id(FISHING_NET).single();
            if (fishNet != null) {
                switch (fishNet.interact("Take")) {
                    case NOT_ON_SCREEN:
                        walking.walk(fishNet);
                        break;
                    case OK:
                        return Utilities.random(800, 1200);
                    default:
                        break;
                }
            } else {
                log("Unable to find fishing net!");
            }
        } else if (widgets.canContinue()) {
            Widget success = widgets.find().text("mmm").single();
            if (success != null && !canLeaveFlag) {
                canLeaveFlag = true;
            }
            if (widgets.canContinue()) {
                if (widgets.clickContinue() == Result.OK) {
                    return Utilities.random(800, 1200);
                }
            }
        } else if (canLeaveFlag) {
            GameObject portal = objects.find("Portal").single();
            if (portal != null) {
                log("Leaving the island");
                switch (portal.interact("Enter")) {
                    case NOT_ON_SCREEN:
                        walking.walk(portal);
                        break;
                    case OK:
                        return Utilities.random(3000, 4600);
                    default:
                        break;
                }
            }
        } else if (inventory.contains("Raw fishlike thing")) {
            Npc bob = npcs.find("Evil bob").single();
            WidgetItem fish = inventory.find("Raw fishlike thing").single();
            if (fish != null) {
                log("Clicking on fish");
                if (fish.interact("Use") == Result.OK) {
                    log("Clicking on bob");
                    camera.setPitch(true);
                    switch (bob.interact("Use")) {
                        case NOT_ON_SCREEN:
                            walking.walk(bob);
                            break;
                        case OK:
                            return Utilities.random(800, 1200);
                        default:
                            break;
                    }
                }
            }
        } else if (!inventory.contains("Fishlike thing")) {
            log("We are fishing the thingy");
            if (fishingSpot == null) {
                log("Grabbing fishing spot");
                fishingSpot = getFishingSpot();
            } else {
                log("Found fishingSpot at " + fishingSpot.getTile());
                if (player.isIdle()) {
                    switch (fishingSpot.interact("Net")) {
                        case NOT_ON_SCREEN:
                            walking.walk(fishingSpot);
                            break;
                        case OK:
                            return Utilities.random(800, 1200);
                        default:
                            break;

                    }
                }
            }
        } else {
            log("Uncooking the fishlike thingy");
            GameObject uncookPot = objects.find("Uncooking pot").single();
            if (uncookPot != null) {
                if (player.isIdle()) {
                    WidgetItem fish = inventory.find("Fishlike thing").single();
                    if (fish != null) {
                        log("Clicking on fish");
                        if (fish.interact("Use") == Result.OK) {
                            log("Clicking on uncooking pot");
                            switch (uncookPot.interact("Use")) {
                                case NOT_ON_SCREEN:
                                    walking.walk(uncookPot);
                                    break;
                                case OK:
                                    return Utilities.random(800, 1200);
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return 100;
    }

    private enum Orientation {
        NORTH,
        SOUTH,
        EAST,
        WEST;
    }

}