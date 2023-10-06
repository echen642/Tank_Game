package TankGameFiles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import edu.macalester.graphics.events.Key;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.events.KeyboardEvent;
import edu.macalester.graphics.Path;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.CanvasWindow;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class is responsible for spawning and moving the Red and Blue Tank
// that will be facing off against against each other.
//
//
// ---------------------------------------------------------------------------------------

public class TankManager {
    private CanvasWindow canvas;
    private List<Tank> tanks;
    private List<Cannon> cannons;
    private Tank redTank;
    private Tank blueTank;
    private Terrain terrain;
    private Point redTankPoint, blueTankPoint, redCannonPoint, blueCannonPoint;
    private String blueCannonPath, redCannonPath;
    private double force;
    private Integer changeInAngle;
    private Integer move;
    private GraphicsText redHPBar, blueHPBar;
    private double tankAngle;
    private ForceMeter forceMeter;
    private ForceMeter blueForceMeter;
    private final double startCannonWidth = 50;
    private final double frame1Width = 60;
    private final double frame2Width = 70;
    private final double frame3Width = 75;

    public TankManager(CanvasWindow canvas, Terrain terrain) {
        tanks = new ArrayList<>();
        cannons = new ArrayList<>();
        redCannonPath = "RedCannon.png";
        blueCannonPath = "BlueCannon.png";
        this.canvas = canvas;
        this.terrain = terrain;
        redTankPoint = new Point(terrain.getTerrainPoint(40).getX(), terrain.getTerrainPoint(40).getY() - 25);
        blueTankPoint = new Point(terrain.getTerrainPoint(260).getX(), terrain.getTerrainPoint(260).getY() - 25);
        redCannonPoint = new Point(redTankPoint.getX() + 40, redTankPoint.getY() + 1);
        blueCannonPoint = new Point(blueTankPoint.getX() - 10, blueTankPoint.getY() + 1);
        redTank = new Tank(redTankPoint, "RedTank.png", redCannonPoint.getX(), redCannonPoint.getY(), redCannonPath,
            Color.RED);
        blueTank = new Tank(blueTankPoint, "BlueTank.png", blueCannonPoint.getX(), blueCannonPoint.getY(),
            blueCannonPath, Color.BLUE);
        generateTanks();
        blueTank.setCenter(blueTank.getPoint());
        centerCannonToTank(blueTank, blueTank.getCannon(), startCannonWidth);
        centerCannonToTank(redTank, redTank.getCannon(), startCannonWidth);
        force = 0;
        forceMeter = new ForceMeter(canvas, 0, 40);
        blueForceMeter = new ForceMeter(canvas, canvas.getWidth() - 400, 40);
        redHPBar = new GraphicsText("5", 20, 120);
        blueHPBar = new GraphicsText("5", canvas.getWidth() - 50, 120);
        redHPBar.setFontSize(50);
        redHPBar.setFillColor(Color.RED);
        blueHPBar.setFontSize(50);
        blueHPBar.setFillColor(Color.BLUE);
        canvas.add(redHPBar);
        canvas.add(blueHPBar);
        blueForceMeter.addToCanvas(canvas);
        forceMeter.addToCanvas(canvas);
    }

    /**
     * Generates blue and red tanks.
     */
    public void generateTanks() {
        for (int i = 0; i < 2; i++) {
            if (i < 1) {
                redTank.setMaxHeight(50);
                redTank.getCannon().setMaxWidth(50);
                canvas.add(redTank.getCannon());
                redTank.setCenter(redTankPoint);
                canvas.add(redTank);
                tanks.add(redTank);
            } else {
                blueTank.getCannon().setAngle(180);
                blueTank.setMaxHeight(50);
                blueTank.getCannon().setMaxWidth(50);
                canvas.add(blueTank.getCannon());
                canvas.add(blueTank);
                tanks.add(blueTank);
                blueTank.switchWorking();
            }
        }
    }

    /**
     * Moves Tank.
     * 
     * @param key
     */
    public void moveTank(KeyboardEvent key) {
        if (key.getKey().equals(Key.valueOf("LEFT_ARROW")) || key.getKey().equals(Key.valueOf("A"))) {
            if ((getWorkingTank().equals(blueTank)) && (blueTank.getCenterX() - 25 > canvas.getWidth() / 2)) {
                setTankPosition(-5);
            }
            if ((getWorkingTank().equals(redTank))
                && (getWorkingTank().getCenterX() - getWorkingTank().getWidth() / 2 > 0)) {
                setTankPosition(-5);
            }
        }

        if (key.getKey().equals(Key.valueOf("RIGHT_ARROW")) || key.getKey().equals(Key.valueOf("D"))) {
            if ((getWorkingTank().equals(redTank)) && (redTank.getCenterX() + 25 < canvas.getWidth() / 2)) {
                setTankPosition(5);
            }
            if ((getWorkingTank().equals(blueTank))
                && (getWorkingTank().getCenterX() + getWorkingTank().getWidth() / 2 < canvas.getWidth())) {
                setTankPosition(5);
            }
        }
    }

    /**
     * Sets the tank Position based on how the tank moves.
     * 
     * @param move
     */
    private void setTankPosition(Integer move) {
        getWorkingTank().setPoint(terrain.getTerrainMovePoint(getWorkingTank().getCenter(), move));
        getWorkingTank().setRotation(tankAngleCalc(-5));
        getWorkingTank().setCenter(workingTankPoint());
        centerCannonToTank(getWorkingTank(), getWorkingCannon(), startCannonWidth);
    }


    /**
     * Changes cannon angle.
     * 
     * @param key
     * @param cannon
     */
    public void tiltCannon(KeyboardEvent key) {
        if ((key.getKey().equals(Key.valueOf("DOWN_ARROW")) || key.getKey().equals(Key.valueOf("S")))
            && getWorkingCannon().getAngle() > 0) {
            setCannonAngle(5);
        }
        if ((key.getKey().equals(Key.valueOf("UP_ARROW")) || key.getKey().equals(Key.valueOf("W")))
            && getWorkingCannon().getAngle() < 180) {
            setCannonAngle(-5);
        }

    }

    /**
     * Sets the cannon angle based on the input integer.
     */
    public void setCannonAngle(Integer changeInAngle) {
        getWorkingCannon().rotateBy(changeInAngle);
        getWorkingCannon().setAngle(getWorkingCannon().getAngle() - changeInAngle);
        centerCannonToTank(getWorkingTank(), getWorkingCannon(), startCannonWidth);
    }

    /**
     * Centers cannon to tank.
     */
    private void centerCannonToTank(Tank tank, Cannon cannon, Double width) {
        cannon.setMaxWidth(width);
        cannon.setCenter(width / 2 * Math.cos(Math.toRadians(cannon.getAngle())) + tank.getCenterX(),
            -width / 2 * Math.sin(Math.toRadians(cannon.getAngle())) + tank.getY() + 7.5);
    }

    /**
     * Fires cannonball, animates cannon, checks if it hit's an object.
     */
    public void fireCannon(KeyboardEvent key) {
        if (getWorkingCannon() != null) {
            Cannonball ball = new Cannonball(
                (25 + (getWorkingCannon().getImageWidth() / 2))
                    * Math.cos(Math.toRadians(getWorkingCannon().getAngle())) + getWorkingTank().getCenterX(),
                (25 + (getWorkingTank().getImageWidth() / 2)) * -Math.sin(Math.toRadians(getWorkingCannon().getAngle()))
                    + getWorkingTank().getY() + 7.5,
                getForce(), getWorkingCannon().getAngle(), canvas.getWidth(), getWorkingTank().getColor());
            if (key.getKey().equals(Key.valueOf("SPACE"))) {
                ball.addToCanvas(canvas);
                animateCannon(key);
                while (ball.updatePosition(0.1)) {
                    if (testHit(ball) == true) {
                        break;
                    }
                    canvas.draw();
                }
                ball.removeFromCanvas(canvas);
            }
            resetForce();
            forceMeter.resetForceMeter();
            blueForceMeter.resetForceMeter();
        }
    }

    /**
     * Tests if the ball that is passed in hits the red tnak the blue tank or the terrain. It returns
     * true if it hits either of these. If it hits a tank, the tank loses a life and the HP counter and
     * HP are updated.
     * 
     * @param ball
     * @return
     */
    public boolean testHit(Cannonball ball) {
        if (intersects(ball) == 0) {
            if (canvas.getElementAt(ball.getBottomPoint()) == redTank
                || canvas.getElementAt(ball.getLeftPoint()) == redTank
                || canvas.getElementAt(ball.getRightPoint()) == redTank
                || canvas.getElementAt(ball.getTopPoint()) == redTank) {
                redTank.reduceHP();
                redHPBar.setText(Integer.toString(redTank.getHP()));
                redTank.getHP();
            }
            if (canvas.getElementAt(ball.getBottomPoint()) == blueTank
                || canvas.getElementAt(ball.getLeftPoint()) == blueTank
                || canvas.getElementAt(ball.getRightPoint()) == blueTank
                || canvas.getElementAt(ball.getTopPoint()) == blueTank) {
                blueTank.reduceHP();
                blueHPBar.setText(Integer.toString(blueTank.getHP()));
                blueTank.getHP();
            }
            return true;
        }
        if (intersects(ball) == 1) {
            terrain.deformTerrain(intersectsPoint(ball));
            return true;
        }
        return false;
    }

    /**
     * Sets Force when space key is pressed and updates the bars on the force meter accordingly.
     */
    public void setForce(KeyboardEvent k) {
        if (k.getKey().equals(Key.valueOf("SPACE"))) {
            force += 2;
            if (forceMeter.getProgressBarScale() * this.force >= forceMeter.getBarWidth()) {
                this.force = forceMeter.getBarWidth() / forceMeter.getProgressBarScale();
            }
            if (getWorkingTank() == redTank) {
                forceMeter.setForce(force);
                forceMeter.updateRedForceMeter();
            } else if (getWorkingTank() == blueTank) {
                blueForceMeter.setForce(force);
                blueForceMeter.updateBlueForceMeter();
            }
        }
    }

    public double getForce() {
        return force;
    }

    public double resetForce() {
        force = 10;
        return force;
    }

    /**
     * Uses side-side-side theorem to calculate an angle using a initial distance input to retrieve
     * other lengths
     */
    public double tankAngleCalc(int move) {
        double a = 0;
        double b = 0;
        double c = 0;

        if (move == -5) {
            a = 5;
            b = findDistance(terrain.getTerrainMovePoint(workingTankPoint(), move));
            c = terrain.getTerrainMovePoint(workingTankPoint(), move).getY() - workingTankPoint().getY();
            if (c < 0) {
                c = Math.abs(terrain.getTerrainMovePoint(workingTankPoint(), move).getY() - workingTankPoint().getY());
                tankAngle = Math.toDegrees(Math.acos(Math.cos((a * a + b * b - c * c) / (2 * a * b))));
                return tankAngle;
            }
            if (c == 0) {
                return 0;
            }
            c = Math.abs(c);
            tankAngle = Math.toDegrees(Math.acos(Math.cos((a * a + b * b - c * c) / (2 * a * b)))) + 270;
            return tankAngle;
        } else {
            a = 5;
            b = findDistance(terrain.getTerrainMovePoint(workingTankPoint(), move));
            c = terrain.getTerrainMovePoint(workingTankPoint(), move).getY() - workingTankPoint().getY();
            if (c < 0) {
                c = Math.abs(terrain.getTerrainMovePoint(workingTankPoint(), move).getY() - workingTankPoint().getY());
                tankAngle = 360 - Math.toDegrees(Math.acos(Math.cos((a * a + b * b - c * c) / (2 * a * b))));
                return tankAngle;
            }
            if (c == 0) {
                tankAngle = 0;
                return tankAngle;
            }
            c = Math.abs(c);
            tankAngle = Math.toDegrees(Math.acos(Math.cos((a * a + b * b - c * c) / (2 * a * b))));
            return tankAngle;
        }
    }

    public double findDistance(Point terrainPoint) {
        return getWorkingTank().getPoint().distance(terrainPoint);
    }

    public Point workingTankPoint() {
        return getWorkingTank().getPoint();
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public Tank getWorkingTank() {
        for (Tank t : tanks) {
            if (t.isWorking()) {
                return t;
            }
        }
        return null;
    }

    /**
     * Returns the tank that is not working.
     */
    public Tank notWorkingTank() {
        for (Tank t : tanks) {
            if (t.notWorking()) {
                return t;
            }
        }
        return null;
    }

    /**
     * Animates the working Cannon by going frame by frame through images when Space.
     */
    public void animateCannon(KeyboardEvent key) {
        if (key.getKey().equals(Key.valueOf("SPACE"))) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        animateWorkingCannonFrame1();
                        sleep(30);
                        animateWorkingCannonFrame2();
                        sleep(30);
                        animateWorkingCannonFrame3();
                        sleep(30);
                        resetToOriginCannon();
                        switchWorkingTank();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            thread.start();
        }
    }

    /**
     * Gets the working tank's cannon and changes the cannon to animate the first frame.
     */
    public void animateWorkingCannonFrame1() {
        if (getWorkingTank() == redTank) {
            redTank.getCannon().setImagePath("RedFireFrame1.png");
            centerCannonToTank(redTank, redTank.getCannon(), frame1Width);
        }
        if (getWorkingTank() == blueTank) {
            blueTank.getCannon().setImagePath("BlueFireFrame1.png");
            centerCannonToTank(blueTank, blueTank.getCannon(), frame1Width);
        }
    }

    /**
     * Gets the working tank's cannon and changes the cannon to animate the second frame.
     */
    public void animateWorkingCannonFrame2() {
        if (getWorkingTank() == redTank) {
            redTank.getCannon().setImagePath("RedFireFrame2.png");
            centerCannonToTank(redTank, redTank.getCannon(), frame2Width);
        }
        if (getWorkingTank() == blueTank) {
            blueTank.getCannon().setImagePath("BlueFireFrame2.png");
            centerCannonToTank(blueTank, blueTank.getCannon(), frame2Width);
        }
    }

    /**
     * Gets the working tank's cannon and changes the cannon to animate the third frame.
     */
    public void animateWorkingCannonFrame3() {
        if (getWorkingTank() == redTank) {
            redTank.getCannon().setImagePath("RedFireFrame3.png");
            centerCannonToTank(redTank, redTank.getCannon(), frame3Width);
        }
        if (getWorkingTank() == blueTank) {
            blueTank.getCannon().setImagePath("BlueFireFrame3.png");
            centerCannonToTank(blueTank, blueTank.getCannon(), frame3Width);
        }
    }

    /**
     * Gets the working tank's cannon and changes the cannon to go back to the origin.
     */
    public void resetToOriginCannon() {
        if (getWorkingTank() == redTank) {
            redTank.getCannon().setImagePath("RedCannon.png");
            centerCannonToTank(redTank, redTank.getCannon(), startCannonWidth);
        }

        if (getWorkingTank() == blueTank) {
            blueTank.getCannon().setImagePath("BlueCannon.png");
            centerCannonToTank(blueTank, blueTank.getCannon(), startCannonWidth);
        }
    }

    /**
     * Switches the working tank.
     */
    public void switchWorkingTank() {
        for (Tank t : tanks) {
            t.switchWorking();
        }
    }

    /**
     * Checks if tank intersects with the top point.
     */
    public int intersects(Cannonball ball) {
        if (canvas.getElementAt(ball.getBottomPoint()) instanceof Tank ||
            canvas.getElementAt(ball.getLeftPoint()) instanceof Tank ||
            canvas.getElementAt(ball.getRightPoint()) instanceof Tank ||
            canvas.getElementAt(ball.getTopPoint()) instanceof Tank) {
            return 0;
        }
        if (canvas.getElementAt(ball.getBottomPoint()) instanceof Path ||
            canvas.getElementAt(ball.getLeftPoint()) instanceof Path ||
            canvas.getElementAt(ball.getRightPoint()) instanceof Path ||
            canvas.getElementAt(ball.getTopPoint()) instanceof Path) {
            return 1;
        }
        return 2;
    }

    public Point intersectsPoint(Cannonball ball) {
        if (canvas.getElementAt(ball.getLeftPoint()) instanceof Path) {
            return ball.getLeftPoint();
        }
        if (canvas.getElementAt(ball.getRightPoint()) instanceof Path) {
            return ball.getRightPoint();
        }
        if (canvas.getElementAt(ball.getTopPoint()) instanceof Path) {
            return ball.getTopPoint();
        }
        return ball.getBottomPoint();
    }

    public Cannon getWorkingCannon() {
        return getWorkingTank().getCannon();
    }

    /**
     * Check how many lives a tank has, if 0 removes the tank.
     */
    public boolean checkLives() {
        for (Tank t : tanks) {
            if (t.getHP() == 0) {
                tanks.remove(t);
                return true;
            }
        }
        return false;
    }

    public void reset() {
        tanks.clear();
        cannons.clear();
    }
}
