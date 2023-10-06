package TankGameFiles;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class sets up the cannonball that will be shot out of a tank's 
// cannon and is responsible for updating the movement of the cannonball on the canvas.
// 
// 
// 
// ---------------------------------------------------------------------------------------


/**
 * Creates a Cannonball that is an ellipse object and moves it on the canvas.
 */
public class Cannonball {
    public static final double GRAVITY = -9.8;
    public static final double RADIUS = 5;
    private double centerX;
    private double centerY;
    // Will take the angle of the cannon. Make sure that we shoot the cannonball out of the TOP of the
    // cannon.
    private double dx;
    private double dy;
    private double maxX;

    private Ellipse ballShape;

    /**
     * Creates a cannonball ellipse object.
     * 
     * @param centerX
     * @param centerY
     * @param initialSpeed
     * @param initialAngle
     * @param maxX
     * @param maxY
     */
    public Cannonball(double centerX, double centerY, double initialSpeed, double initialAngle, double maxX,
        Color color) {
        this.centerX = centerX;
        this.centerY = centerY;

        this.maxX = maxX;

        double angleInRadians = Math.toRadians(initialAngle);
        this.dx = initialSpeed * Math.cos(angleInRadians);
        this.dy = initialSpeed * -Math.sin(angleInRadians);

        ballShape = new Ellipse(centerX - RADIUS, centerY - RADIUS, 2 * RADIUS, 2 * RADIUS);
        ballShape.setFillColor(color);
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    /**
     * Updates the position of the cannon ball according to velocity and gravity. Doesn't update if past
     * canvas X limits and below bottom Y limit.
     */
    public boolean updatePosition(double dt) {
        double xCoord = getCenterX() + (dx * dt);
        double yCoord = getCenterY() + (dy * dt);

        if (xCoord > 0 && xCoord < maxX) {
            if (yCoord > 0 || yCoord < 0) {
                centerX = xCoord;
                centerY = yCoord;
                ballShape.setPosition(centerX, centerY);
                dy = dy - (GRAVITY * dt);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean addToCanvas(CanvasWindow canvas) {
        canvas.add(ballShape);
        return true;
    }

    public boolean removeFromCanvas(CanvasWindow canvas) {
        canvas.remove(ballShape);
        return false;
    }

    /**
     * Gets topmost point of the ball.
     */
    public Point getTopPoint() {
        return new Point(getCenterX(), getCenterY() - RADIUS);
    }

    /**
     * Gets leftmost point of the ball.
     */
    public Point getLeftPoint() {
        return new Point(getCenterX() - RADIUS, getCenterY());
    }

    /**
     * Gets rightmost point of the ball.
     */
    public Point getRightPoint() {
        return new Point(getCenterX() + RADIUS, getCenterY());
    }

    /**
     * Gets bottommost point of the ball.
     */
    public Point getBottomPoint() {
        return new Point(getCenterX(), getCenterY() + RADIUS);
    }
}
