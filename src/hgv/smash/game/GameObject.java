package hgv.smash.game;

import java.awt.*;

public abstract class GameObject {

    public abstract void collide(GameObject go);

    public abstract void calc(long millis);

    public abstract void draw(Graphics2D graphics2D);
}
