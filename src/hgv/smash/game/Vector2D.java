package hgv.smash.game;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v1, Vector2D v2) {
        x = v2.x - v1.x;
        y = v2.y - v1.y;
    }

    public Vector2D(Point2D point2D) {
        x = point2D.getX();
        y = point2D.getY();
    }

    public Vector2D(Point2D p1, Point2D p2) {
        Vector2D v1 = new Vector2D(p1);
        Vector2D v2 = new Vector2D(p2);

        x = v2.x - v1.x;
        y = v2.y - v1.y;
    }

    public Vector2D(Line2D line) {
        Point2D p1 = line.getP1();
        Point2D p2 = line.getP2();

        Vector2D v1 = new Vector2D(p1);
        Vector2D v2 = new Vector2D(p2);

        x = v2.x - v1.x;
        y = v2.y - v1.y;
    }

    public static Vector2D directionVector(double angle) {
        double x = Math.cos(angle);
        double y = Math.sin(angle);

        return new Vector2D(x, y);
    }

    public Vector2D add(Vector2D vector) {
        return new Vector2D(x + vector.x, y + vector.y);
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

    public Vector2D rotate(double rad) {
        double x = this.x * Math.cos(rad) - this.y * Math.sin(rad);
        double y = this.x * Math.sin(rad) + this.y * Math.cos(rad);
        return new Vector2D(x, y);
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
