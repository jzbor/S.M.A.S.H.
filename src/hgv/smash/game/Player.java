package hgv.smash.game;

import hgv.smash.Main;
import hgv.smash.resources.Avatar;

import java.awt.*;

public class Player extends GameObject {

    private static final int WIDTH = 60; // width of model
    private static final int HEIGHT = 100; // height of model
    private static final double SPEED = 0.5; // speed of xpos movement (also used by jump())
    private int jumps = 2; // jumps left
    private int xpos, ypos; // position of player (usually upper left corner of model)
    private double vx, vy; // speed of player
    private Avatar avatar; // avatar to display for player
    private Shape model; // model (mainly for collision detection)

    public Player(Avatar avatar, int xpos) {
        this.xpos = xpos;
        this.ypos = 50;
        vx = 0;
        vy = 0;
        this.avatar = avatar;
        this.model = new Rectangle(xpos, ypos, WIDTH, HEIGHT);
    }

    @Override
    public void collide(GameObject go) {
        if (go instanceof LevelMap) {
            LevelMap levelMap = (LevelMap) go;
            if (levelMap.intersects(model)) {
                vx = 0;
                vy = 0;
                // reset jumps
                jumps = 2;
            }
        }
    }

    @Override
    public void calc(long millis) {
        double factor = 0.00005;

        // falling
        vy += millis * 9.81 * factor;

        // calc position by velocity
        ypos = (int) (ypos + vy * millis);
        xpos = (int) (xpos + vx * millis);

        // update model
        this.model = new Rectangle(xpos, ypos, WIDTH, HEIGHT);
    }

    public void walkLeft() {
        this.vx = -SPEED;
    }

    public void walkRight() {
        this.vx = +SPEED;
    }

    public void jump() {
        // jump only limited times
        if (jumps > 0) {
            // speed up to the north
            this.vy = -SPEED * 1;
            jumps--;
        }
    }

    public void stay() {
        // reset x velocity
        this.vx = 0;
    }


    @Override
    public void draw(Graphics2D graphics2D) {
        // @TODO change to drawing the avatar

        if (Main.DEBUG) {
            // for debugging purposes only:
            graphics2D.setColor(Color.RED);
            graphics2D.fillRect(xpos, ypos, WIDTH, HEIGHT);
        }
    }
}
