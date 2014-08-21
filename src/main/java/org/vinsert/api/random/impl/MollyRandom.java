package org.vinsert.api.random.impl;

import org.vinsert.api.collection.Filter;
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
import org.vinsert.api.wrappers.Widget;

import java.awt.*;

@LoginRequired
@RandomManifest(name = "Molly Random Solver", author = "ande, hancock", version = 0.1)
public final class MollyRandom extends RandomSolver {

    private Npc molly, suspect;
    private GameObject claw, controlPanel;
    private int modelId = 0;
    private boolean usingControls = false, inRoom = false, done = false;
    private int suspectX, suspectY, clawX, clawY;
    private String debug = "";

    @Override
    public boolean canRun() {
        return npcs.find("Molly").single() != null || npcs.find("Suspect").single() != null;
    }

    @Override
    public void reset(){
        molly = null;
        suspect = null;
        claw = null;
        controlPanel = null;
        modelId = 0;
        usingControls = false;
        inRoom = false;
        done = false;
        debug = "";
    }

    @Override
    public int run() {
        if(!done) {
            controlPanel = objects.find("Control panel").single();
            usingControls = widgets.find().group(240).single() != null;
            claw = objects.find("Evil claw").single();
            suspect = npcs.find().filter(new Filter<Npc>() {
                @Override
                public boolean accept(Npc acceptable) {
                    return acceptable.getComposite().getModelIds()[0] == modelId;
                }
            }).single();

            if (modelId == 0) {
                debug = "Getting model id";
                molly = npcs.find("Molly").single();
                if (molly != null) {
                    modelId = molly.getComposite().getModelIds()[0];
                }
            } else if (!inRoom) {
                debug = "Entering room";
                GameObject door = objects.find("Door").single();
                if (door.isValid()) {
                    switch (door.interact("Open")) {
                        case NOT_ON_SCREEN:
                            camera.turnTo(door);
                            break;
                    }
                    Utilities.sleep(1200,1500);
                }
                while (inDialogue()) {
                    completeDialogue();
                }
                if (walking.canReach(controlPanel)) {
                    inRoom = true;
                }
            }

            if (inRoom) {
                debug = "Catching Evil Twin";
                catchTwin();
            }

        }else{
            debug = "Finishing";
            molly = npcs.find("Molly").single();
            if(molly != null) {
                if (!walking.canReach(molly)) {
                    GameObject door = objects.find("Door").single();
                    if (door.isValid()) {
                        switch (door.interact("Open")) {
                            case NOT_ON_SCREEN:
                                camera.turnTo(door);
                                break;
                        }
                        Utilities.sleep(1200);
                    }
                } else {
                    molly.interact("Talk-to");
                    while (inDialogue()) {
                        completeDialogue();
                    }
                }
            }
        }
        return 200;
    }

    public void catchTwin(){
        if(suspect != null) {
            suspectX = suspect.getX();
            suspectY = suspect.getY();
        }
        if(claw != null) {
            clawX = claw.getX();
            clawY = claw.getY();
        }

        if(controlPanel != null && !usingControls){
            switch (controlPanel.interact("Use")){
                case NOT_ON_SCREEN:
                    camera.turnTo(controlPanel);
                    break;
            }
        }else{
            if(suspectX - clawX > 0){
                useControl("left");
            }else if(suspectX - clawX < 0){
                useControl("right");
            }

            if(suspectY - clawY > 0){
                useControl("down");
            }else if(suspectY - clawY < 0){
                useControl("up");
            }
            if(clawX == suspectX && clawY == suspectY){
                useControl("catch");
            }
        }
    }

    public void useControl(String direction){
        switch (direction){
            case "up":
                widgets.find(240,6).single().interact("Ok");
                break;
            case "down":
                widgets.find(240,11).single().interact("Ok");
                break;
            case "left":
                widgets.find(240,16).single().interact("Ok");
                break;
            case "right":
                widgets.find(240,21).single().interact("Ok");
                break;
            case "catch":
                widgets.find(240,23).single().interact("Ok");
                break;
        }
        Utilities.sleep(200);
    }

    public void completeDialogue(){
        if(widgets.canContinue()){
            widgets.clickContinue();
            Utilities.sleep(600);
        }
        Widget option = widgets.find().text("Yes").single();
        if(option != null && option.isValid()){
            option.interact("Continue");
        }
    }

    public boolean inDialogue(){
        return widgets.find(241,0).single() != null || widgets.find(242,0).single() != null || widgets.canContinue() || widgets.find().text("Select an Option").single() != null;
    }

    @EventHandler
    public void onMessage(MessageEvent evt) {
        if(evt.getMessage().equals("You caught the Evil twin!")){
            done = true;
        }
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        Graphics2D g = (Graphics2D) event.getGraphics();
        g.setColor(Color.YELLOW);
        g.drawString("Solving Molly.",10,80);
        g.drawString(debug,10,100);
    }


}

