package hgv.smash.game;

import hgv.smash.resources.Avatar;

import java.awt.*;

public class Player extends GameObject {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 100;
    private static final double SPEED = 0.5;
    private int x, y;
    private double vx, vy;
    private Avatar avatar;
    private Shape model;

    public Player(Avatar avatar, int x) {
        this.x = x;
        y = 50;
        vx = 0;
        vy = 0;
        this.avatar = avatar;
        this.model = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void collide(GameObject go) {
        if (go instanceof LevelMap) {
            LevelMap levelMap = (LevelMap) go;
            if (levelMap.intersects(model)) {
                vx = 0;
                vy = 0;
            }
        }
    }

    @Override
    public void calc(long millis) {
        double factor = 0.00005;
        vy += millis * 9.81 * factor;

        y = (int) (y + vy * millis);
        x = (int) (x + vx * millis);

        this.model = new Rectangle(x, y, WIDTH, HEIGHT);

    }

    public void walkLeft() {
        this.vx = -SPEED;
    }

    public void walkRight() {
        this.vx = +SPEED;
    }

    public void jump() {
        this.vy = -SPEED * 1;
    }

    public void stay() {
        this.vx = 0;
    }


    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.RED);
        graphics2D.fillRect(x, y, WIDTH, HEIGHT);
    }
}
