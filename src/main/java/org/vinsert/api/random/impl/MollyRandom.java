package org.vinsert.api.random.impl;

import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.queries.GameObjectQuery;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.MessageEvent;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomManifest;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.GameObject;
import org.vinsert.api.wrappers.Npc;
import org.vinsert.api.wrappers.Tile;

import java.awt.*;

@LoginRequired
@RandomManifest(name = "Molly Random Solver", author = "hancock", version = 0.1)
public final class MollyRandom extends RandomSolver {

    private Npc mollyEntity;
    private int modelId = 0;
    boolean done = false;
    Tile startTile = null;
    Tile suspectTile = null;
    boolean reach = false;

    @Override
    public boolean canRun() {
        if (!npcs.find("Molly").exists() && !npcs.find("Suspect").exists()) {
            startTile = null;
            suspectTile = null;
            modelId = 0;
            mollyEntity = null;
            reach = false;
            done = false;
        }
        return npcs.find("Molly").single() != null || npcs.find("Suspect").single() != null;
    }

    @Override
    public int run() {
        if (!player.isIdle()) {
            return 200;
        }
        if (modelId == 0) {
            mollyEntity = npcs.find("Molly").single();
            if (mollyEntity != null) {
                modelId = mollyEntity.getComposite().getModelIds()[0];
            }
            return 100;
        }
        if (widgets.canContinue()) {
            widgets.clickContinue();
            return 500;
        }
        if (widgets.find().text("Yes, I know").visible().exists()) {
            widgets.find().text("Yes, I know").visible().single().click(true);
            return 400;
        }
        if (done && !reach) {
            switch (npcs.find("Molly").single().interact("Talk-to")) {
                case OK:
                    Utilities.sleepUntil(new StatePredicate() {
                        @Override
                        public boolean apply() {
                            return player.isIdle();
                        }
                    }, 2000);
                    return 500;
                default:
                    reach = true;
            }
            return 500;
        }
        GameObject panel = objects.find("control panel").single();
        if (panel.distanceTo(player.getTile()) > 2 && (reach ||
                npcs.find("Molly").distance(4).exists()) || reach || done) {
            GameObjectQuery doorQ = objects.find("Door");
            if (doorQ.exists()) {
                switch (doorQ.single().interact("Open")) {
                    case OK:
                        reach = false;
                        return 200;
                    default:
                        return 200;
                }
            }
        }
        if (!done) {
            if (widgets.getGroup(240) == null ||
                    !widgets.getGroup(240).isValid()) {
                panel.interact("Use");
                return 400;
            }
            GameObject ob = objects.find("Evil claw").single();
            if (ob == null) {
                return 500;
            }
            startTile = ob.getTile();
            if (widgets.getGroup(240) != null && widgets.getGroup(240).isValid()) {
                for (Npc n : npcs.find()) {
                    if (n.getComposite().getModelIds()[0] == modelId) {
                        suspectTile = n.getTile();
                    }
                }
                if (suspectTile != null) {
                    if (startTile != null) {
                        if (startTile.getX() - suspectTile.getX() > 0) {
                            widgets.find(240, 32).single().interact("Ok");
                        } else if (startTile.getX() - suspectTile.getX() < 0) {
                            widgets.find(240, 31).single().interact("Ok");

                        } else if (startTile.getY() - suspectTile.getY() > 0) {
                            widgets.find(240, 29).single().interact("Ok");
                        } else if (startTile.getY() - suspectTile.getY() < 0) {
                            widgets.find(240, 30).single().interact("Ok");
                        } else {
                            widgets.find(240, 24).single().interact("Ok");
                        }

                    }
                }
            }
        } else {
            if (mollyEntity != null && walking.canReach(mollyEntity.getTile())) {
                if (widgets.canContinue()) {
                    widgets.clickContinue();
                } else {
                    switch (mollyEntity.interact("talk-to")) {
                        case NOT_ON_SCREEN:
                            camera.turnTo(mollyEntity);
                            break;

                    }
                }
            }
        }
        return 600;
    }

    @EventHandler
    public void message(MessageEvent evt) {
        if (evt.getMessage().contains("can't reach")) {
            reach = true;
        }
        if (evt.getMessage().contains("You caught the evil")) {
            done = true;
            reach = false;
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics2D g = (Graphics2D) event.getGraphics();
        if (suspectTile != null) {
            g.drawString("my suspect", viewport.convert(suspectTile).x, viewport.convert(suspectTile).y);
        }
        if (startTile != null) {
            g.drawString("my position", viewport.convert(startTile).x, viewport.convert(startTile).y);
        }
        g.drawString("Done: " + done, 10, 80);
    }

}

