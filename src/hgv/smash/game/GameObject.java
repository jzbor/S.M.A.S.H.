package hgv.smash.game;

import java.awt.*;

public abstract class GameObject {

    private static final int meter = 50;

    public abstract void collide(GameObject go);

    public abstract void calc(long millis);

    public abstract void draw(Graphics2D graphics2D);

    public static double convertMetric(double mOverS) {
        // Converts m/s to px/ms;
        return mOverS * meter * 1000.0;
    }
}
