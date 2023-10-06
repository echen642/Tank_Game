package TankGameFiles;

import java.awt.Color;

import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class is responsible for spawning a tank on the screen and
// keeping track of the tank's HP and active status.
// 
// 
// ---------------------------------------------------------------------------------------

public class Tank extends Image {
    private boolean working;
    private Cannon cannon;
    private Point point;
    private int hp = 5;
    private String imagePath;
    private Color color;

    /**
     * Creates a Tank Object.
     * 
     * @param x
     * @param y
     * @param imagePath
     * @param cannonX
     * @param cannonY
     * @param cannonPath
     */
    public Tank(Point point, String imagePath, double cannonX, double cannonY, String cannonPath, Color color) {
        super(point.getX(), point.getY(), imagePath);
        this.point = point;
        this.imagePath = imagePath;
        this.color = color;
        cannon = new Cannon(cannonX, cannonY, cannonPath);
    }

    public int getHP() {
        return hp;
    }

    public int reduceHP() {
        return hp -= 1;
    }

    public Cannon getCannon() {
        return cannon;
    }

    public boolean isWorking() {
        return working;
    }

    public boolean notWorking() {
        return !working;
    }

    public void switchWorking() {
        working = !working;
    }

    public double getCenterX() {
        return getPosition().getX() + getWidth() / 2;
    }

    public double getCenterY() {
        return getPosition().getY() + getHeight() / 2;
    }

    public String toString() {
        return imagePath;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point p) {
        point = p;
    }

    public Color getColor() {
        return this.color;
    }
}
