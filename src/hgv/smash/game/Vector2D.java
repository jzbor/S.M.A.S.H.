package hgv.smash.game;

import java.awt.geom.Point2D;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Point2D point2D) {
        x = point2D.getX();
        y = point2D.getY();
    }

    public Vector2D multiplyWithScalar(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }

    public double scalarProduct(Vector2D vector) {
        return x * vector.x + y * vector.y;
    }

    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double angle(Vector2D vector) {
        double scalarProduct = scalarProduct(vector);
        return Math.acos(scalarProduct / (norm() * vector.norm()));
    }

    public Vector2D directionVector() {
        return multiplyWithScalar(1 / norm());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + x + "|" + y + ")";
    }
}
