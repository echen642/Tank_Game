package TankGameFiles;

import edu.macalester.graphics.Image;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class sets up the cannon that will be used by the tank and is
// responsible for keeping track of the cannon's angle.
// 
// 
// ---------------------------------------------------------------------------------------

public class Cannon extends Image {

    private double angle = 0; // Is in radians.


    public Cannon(double x, double y, String imagePath) {
        super(x, y, imagePath);
    }

    /**
     * Returns the cannon's angle in radians.
     */
    public double getAngle() {
        return angle;
    }

    public void setAngle(double input) {
        angle = input;
    }
}
