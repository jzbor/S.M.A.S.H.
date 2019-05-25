package hgv.smash.game;

import hgv.smash.resources.Avatar;

import java.awt.*;

public class Player extends GameObject {

    private int x, y;
    private double vx, vy;
    private Avatar avatar;
    private Shape model;

    public Player(Avatar avatar) {
        this.avatar = avatar;
        this.model = new Rectangle(0, 0, 10, 10);
    }

    @Override
    public void collide(GameObject go) {
        if (go instanceof LevelMap) {
            LevelMap levelMap = (LevelMap) go;
        }
    }

    @Override
    public void calc(long millis) {

    }

    @Override
    public void draw(Graphics2D graphics2D) {

    }
}
