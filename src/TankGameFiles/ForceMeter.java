package TankGameFiles;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;
import java.awt.Color;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class sets up the force meters that will show the force of the
// cannonball for the Red and Blue tanks.
//
// ---------------------------------------------------------------------------------------

public class ForceMeter extends GraphicsGroup {
    private double force;
    private Rectangle currentForceMeter;
    private Rectangle forceMeter;
    private CanvasWindow canvas;
    private final double SCALE = 3;
    private final double BAR_WIDTH = 400;
    private final double BAR_HEIGHT = 20;

    public ForceMeter(CanvasWindow canvas, double xCoord, double yCoord) {
        this.canvas = canvas;
        forceMeter = new Rectangle(xCoord, yCoord, BAR_WIDTH, BAR_HEIGHT);
        currentForceMeter = new Rectangle(xCoord, yCoord, 0, BAR_HEIGHT);
    }

    public double getBarWidth() {
        return BAR_WIDTH;
    }

    public double getProgressBarScale() {
        return SCALE;
    }

    /**
     * Sets the force of the cannon shot.
     */
    public void setForce(double force) {
        this.force = force;
        if (SCALE * this.force >= BAR_WIDTH) {
            this.force = BAR_WIDTH / SCALE;
        }
    }

    public double getForce() {
        return force;
    }

    /**
     * Updates the red force meter.
     */
    public void updateRedForceMeter() {
        currentForceMeter.setSize(SCALE * force, BAR_HEIGHT);
        currentForceMeter.setFillColor(Color.RED);
        canvas.add(currentForceMeter);
    }

    /**
     * Updates the blue force meter.
     */
    public void updateBlueForceMeter() {
        currentForceMeter.setSize(SCALE * force, BAR_HEIGHT); // Blue Force Meter should expand from Right-to-Left, not
                                                              // Left-to-Right
        currentForceMeter.setFillColor(Color.BLUE);
        canvas.add(currentForceMeter);
    }

    public void resetForceMeter() {
        currentForceMeter.setSize(0, 0);
    }

    public void addToCanvas(CanvasWindow canvas) {
        canvas.add(forceMeter);
    }
}
