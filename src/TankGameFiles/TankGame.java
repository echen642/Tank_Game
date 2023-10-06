package TankGameFiles;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.ui.Button;

// ---------------------------------------------------------------------------------------
// Author: Eddie Chen, Bram Nutt, and Marcus Monk Wallace
// Description: This class sets up the canvas that the game will take place in and
// is also responsible for running opening-ending "Tanks!" game loop.
//
//
// ---------------------------------------------------------------------------------------

public class TankGame {
    private final int CANVAS_HEIGHT = 600;
    private final int CANVAS_WIDTH = 1200;
    private TankManager tankManager;
    private CanvasWindow canvas;
    private Terrain terrain;
    private int numPlays = 0;

    public TankGame() {
        canvas = new CanvasWindow("Tanks!", CANVAS_WIDTH, CANVAS_HEIGHT);
        openingScreen();
    }

    /**
     * Creates background for the game.
     */
    private void createBackground() {
        Image sky = new Image(0, -300, "background.jpg");
        sky.setMaxHeight(CANVAS_HEIGHT);
        sky.setMaxWidth(CANVAS_WIDTH);
        sky.setScale(3);
        canvas.add(sky);
    }

    public void tankControls(Tank tank, Cannon cannon) {
        canvas.onKeyDown(event -> tankManager.moveTank(event));
    }

    public void cannonControls(Tank tank, Cannon cannon) {
        canvas.onKeyDown(event -> tankManager.tiltCannon(event));
        canvas.onKeyDown(event -> tankManager.setForce(event));
        canvas.onKeyUp(event -> {
            tankManager.fireCannon(event);
            if (tankManager.checkLives()) {
                endScreen();
            }
        });
    }

    private void endScreen() {
        canvas.removeAll();

        String winner = tankManager.getTanks().get(0).toString().replace("Tank.png", " Tank");
        String winningTank = tankManager.getTanks().get(0).toString();
        GraphicsText winnerMessage = new GraphicsText(winner + " wins!");
        Button quitButton = new Button("Quit");
        Button playButton = new Button("Play Again!");
        createBackground();

        if (winningTank == "RedTank.png") {
            Image redTankImage = new Image("RedTank.png");
            Image redTankCannonImage = new Image("RedCannon.png");
            redTankImage.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 3 + 15);
            redTankCannonImage.setX(redTankImage.getX() + 70);
            redTankCannonImage.setY(redTankImage.getY() + 7);
            canvas.add(redTankCannonImage);
            canvas.add(redTankImage);
            tankManager.reset();
        }

        if (winningTank == "BlueTank.png") {
            Image blueTankImage = new Image("BlueTank.png");
            Image blueTankCannonImage = new Image("BlueCannon.png");
            blueTankImage.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 3 + 15);
            blueTankCannonImage.setX(blueTankImage.getX() - 20);
            blueTankCannonImage.setY(blueTankImage.getY() + 7);
            canvas.add(blueTankCannonImage);
            canvas.add(blueTankImage);
            tankManager.reset();
        }

        winnerMessage.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 3 - 50);
        quitButton.setCenter(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2);
        playButton.setCenter(canvas.getWidth() / 2 + 100, canvas.getHeight() / 2);
        // Call Opening Screen
        playButton.onClick(() -> {
            canvas.removeAll();
            openingScreen();
        });
        quitButton.onClick(() -> canvas.closeWindow());
        canvas.add(quitButton);
        canvas.add(playButton);
        canvas.add(winnerMessage);
        canvas.draw();
    }

    /**
     * Creates the Opening Screen.
     */
    public void openingScreen() {
        Image redTankImage = new Image("RedTank.png");
        Image redTankCannonImage = new Image("RedCannon.png");
        Image blueTankImage = new Image("BlueTank.png");
        Image blueTankCannonImage = new Image("BlueCannon.png");
        Image tankLogo = new Image("tankLogo.png");
        createBackground();
        Button gameButton = new Button("Start Game!");
        gameButton.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
        gameButton.onClick(() -> {
            canvas.removeAll();
            createBackground();
            terrain = new Terrain(canvas, CANVAS_WIDTH, 400);
            tankManager = new TankManager(canvas, terrain);
            terrain.generateTerrain();
            canvas.draw();
            if (numPlays == 0) {
                tankControls(tankManager.getWorkingTank(), tankManager.getWorkingCannon());
                cannonControls(tankManager.getWorkingTank(), tankManager.getWorkingCannon());
                numPlays += 1;
            }
        });
        canvas.add(gameButton);
        redTankImage.setCenter(CANVAS_WIDTH / 6, CANVAS_HEIGHT / 2);
        redTankCannonImage.setX(redTankImage.getX() + 70);
        redTankCannonImage.setY(redTankImage.getY() + 7);
        blueTankImage.setCenter(CANVAS_WIDTH * 0.9, CANVAS_HEIGHT / 2);
        blueTankCannonImage.setX(blueTankImage.getX() - 20);
        blueTankCannonImage.setY(blueTankImage.getY() + 7);
        tankLogo.setCenter(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 50);
        canvas.add(blueTankCannonImage);
        canvas.add(blueTankImage);
        canvas.add(redTankCannonImage);
        canvas.add(redTankImage);
        canvas.add(tankLogo);
    }

    public static void main(String[] args) {
        new TankGame();
    }
}
