package TankGameFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Path;
import edu.macalester.graphics.Point;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class generates random terrain to be used for the game and
// is responsible for keeping track of the vertices on the terrain.
//
//
// ---------------------------------------------------------------------------------------

public class Terrain extends GraphicsGroup {
    private List<Point> points;
    private CanvasWindow canvas;
    private Random random;
    private Path path;


    public Terrain(CanvasWindow canvas, double bounds, double yAxis) {
        this.canvas = canvas;
        points = new ArrayList<>();
        random = new Random();
        path = new Path(points, false);
        int down = 6;
        int up = 6;
        Point init = new Point(0, canvas.getHeight());
        points.add(init);
        for (double xCoord = 0; xCoord < bounds; xCoord += 5) {
            if (yAxis > 570) {
                yAxis = 570;
            }
            if (yAxis < 200) {
                yAxis = 200;
            }
            if (down < 5) {
                yAxis += 5;
                Point point = new Point(xCoord, yAxis);
                points.add(point);
                down++;
            }
            if (up < 5) {
                yAxis -= 5;
                Point point = new Point(xCoord, yAxis);
                points.add(point);
                up++;
            }
            if (xCoord % 100 == 0) {
                double randomInt = random.nextInt(20 + 20) - 20;
                yAxis += randomInt;
                Point point = new Point(xCoord, yAxis);
                points.add(point);
                if (randomInt > 0) {
                    down = 0;
                }
                if (randomInt < 0) {
                    up = 0;
                }
            } else {
                Point point = new Point(xCoord, yAxis);
                points.add(point);
            }
        }
        Point end = new Point(canvas.getWidth(), canvas.getHeight());
        points.add(end);
    }

    public Point getTerrainPoint(int index) {
        return points.get(index);
    }

    /**
     * Gets the new point on the path from an existing point.
     * 
     * @param point existing point
     * @param x     move point
     * @return
     */
    public Point getTerrainMovePoint(Point point, double x) {
        Point newPoint = new Point(point.getX() + x, point.getY());
        for (Point p : points) {
            if (Math.round(p.getX()) == Math.round(newPoint.getX())) {
                newPoint = new Point(p.getX(), p.getY() - 25);
            }
        }
        return newPoint;
    }

    /**
     * Changes existing points on the path by using an initial deform point
     * 
     * @param point cannonball collision part
     */
    public void deformTerrain(Point point) {
        double checkPoint = point.getX();
        int deformCenter = 0;
        for (Point p : points) {
            if (Math.abs(point.getX() - p.getX()) < checkPoint) {
                checkPoint = Math.abs(point.getX() - p.getX());
                deformCenter = points.indexOf(p);
            }
        }
        if (points.get(deformCenter).getY() <= 570) {
            points.set(deformCenter, points.get(deformCenter).withY(points.get(deformCenter).getY()+10));
            for (int i = 1; i < 6; i++){
                if (deformCenter + i < points.size()) {
                    points.set(deformCenter+i, points.get(deformCenter+i).withY(points.get(deformCenter+i).getY()+10-(i*2)));
                }
                if (deformCenter - i > 0) {
                    points.set(deformCenter-i, points.get(deformCenter-i).withY(points.get(deformCenter-i).getY()+10-(i*2)));
                }
            }
            path.setVertices(points);
        }
    }

    /**
     * Generates the terrain by using a list of points.
     */
    public void generateTerrain() {
        path.setVertices(points);
        canvas.add(path);
        canvas.draw();
    }
}
