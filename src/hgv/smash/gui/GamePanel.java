package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.GameloopThread;
import hgv.smash.game.LevelMap;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Music;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;

public class GamePanel extends Panel {


    private static final int FRAMERATE = 100;
    //size of frame
    private double width;
    private double height;
    //keys for movement
    private static long JUMP_COOLDOWN = 2000;
    // y coords defining a death
    private static int RANGE_OF_DEATH = 1000;
    //player1
    private char[] keys_player_1 = {'w', 'a', 'd', 'f'};
    private boolean[] booleans_player1 = {false, false, false, false};
    //player2
    private char[] keys_player_2 = {'i', 'j', 'l', 'ö'};
    private boolean[] booleans_player2 = {false, false, false, false};
    //actions for keys in same order as keys
    private int[] actions = {Player.Movement.JUMP, Player.Movement.MOVE_LEFT, Player.Movement.MOVE_RIGHT, Player.Movement.NORMAL_HIT};
    //last performed action by pressing
    private int lastChangePlayer1 = Player.Movement.STOP_MOVING;
    private int lastChangePlayer2 = Player.Movement.STOP_MOVING;
    private boolean running;
    private int currentFramerate;
    private Player player1;
    private Player player2;
    private LevelMap levelMap;
    private BufferedImage frameBuffer;

    public GamePanel(Avatar a1, Avatar a2, LevelMap map) {
        height = Frame.getInstance().getHeight();
        width = Frame.getInstance().getWidth();
        // assign and create params
        running = true;
        GameloopThread gameloopThread = new GameloopThread(this);
        player1 = new Player(a1, 200, map);
        player2 = new Player(a2, 300, map);
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
        levelMap = map;

        OurKeyListener.getInstance().setPanel(this);

        // start gameloop
        gameloopThread.start();
    }

    public void gameloop() {
        long lastFrame = System.currentTimeMillis() - 1;
        while (running) {
            // get time for the physics to calculate
            long thisFrame = System.currentTimeMillis();
            long timedelta = thisFrame - lastFrame;
            currentFramerate = (int) (1000 / timedelta);

            // detect death
            detectGameover();

            player1.calc(timedelta);
            player2.calc(timedelta);
            levelMap.calc(timedelta);

            // collision detection
            //player1.collide(levelMap);
            //player2.collide(levelMap);

            // buffer frame
            BufferedImage bi = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics2D = (Graphics2D) bi.getGraphics();

            // draw items on buffer
            levelMap.draw(graphics2D);
            player1.draw(graphics2D);
            player2.draw(graphics2D);

            if (Main.DEBUG) {
                // draw fps
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(currentFramerate + " FPS", 20, 20);
            }

            // @TODO implement thread safety
            frameBuffer = bi;

            // request ui update
            updateUI();

            // sleep
            if ((System.currentTimeMillis() - thisFrame) < (1000.0 / FRAMERATE)) {
                int sleep = (int) ((1000.0 / FRAMERATE) - (System.currentTimeMillis() - thisFrame));
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lastFrame = thisFrame;
        }
    }

    private void detectGameover() {
        boolean gameover = false;
        Player p = null;
        if (player1.getYPos() > RANGE_OF_DEATH) {
            p = player2;
            gameover = true;
        } else if (player2.getYPos() > RANGE_OF_DEATH) {
            p = player1;
            gameover = true;
        }

        if (gameover) {
            BufferedImage img = new BufferedImage(getHeight(), getWidth(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D imageGraphics = img.createGraphics();
            printAll(imageGraphics);
            imageGraphics.dispose();

            Panel panel = new ScorePanel(p, p.getOtherPlayer(), img);
            Frame.getInstance().getContentPane().removeAll();
            Frame.getInstance().getContentPane().add(panel);
            Frame.getInstance().repaint();
            panel.updateUI();

            Music.getInstanceGameMusic().stop();
            running = false;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // paint buffer
        Graphics2D graphics2D = (Graphics2D) graphics;
        //graphics2D.drawImage(frameBuffer, 0, 0, this);
        Image image = null;
        if(frameBuffer!=null){
            image = claculateCamera();
        }else {
            image = frameBuffer;
        }
        //graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);

        graphics2D.drawImage(image, 0, 0, this);
    }

    public Image claculateCamera() {
        int offset = 100;


        //find smallest rectangle wich has both players with offset inside
        double yTop;
        if (player1.getYPos() > player2.getYPos()) {
            yTop = player2.getYPos();
        } else {
            yTop = player1.getYPos();
        }
        yTop -= offset;

        double yBottom;
        if (player1.getYPos() + player1.getHeight() > player2.getYPos() + player2.getHeight()) {
            yBottom = player1.getYPos() + player1.getHeight();
        } else {
            yBottom = player2.getYPos() + player2.getHeight();
        }
        yBottom += offset;


        double xLeft;
        if (player1.getXPos() > player2.getXPos()) {
            xLeft = player2.getXPos();
        } else {
            xLeft = player1.getXPos();
        }
        xLeft -= offset;

        double xRight;
        if (player1.getXPos() + player1.getWidth() > player2.getXPos() + player2.getWidth()) {
            xRight = player1.getXPos() + player1.getWidth();
        } else {
            xRight = player2.getXPos() + player2.getWidth();
        }
        xRight += offset;


        //calculate length of side of smallest rectangle and increase smaller size till it fits to relation of window
        double xDiff = xRight - xLeft;
        double yDiff = yBottom - yTop;

        if (xDiff / yDiff < 1024.0 / 768.0) {
            double xDiffOld = xDiff;
            xDiff = 1024.0 / 768.0 * yDiff;
            double xAdd = (xDiff - xDiffOld) / 2;
            xLeft -= xAdd;
        } else {
            double yDiffOld = yDiff;
            yDiff = 768.0 / 1024.0 * xDiff;
            double yAdd = (yDiff - yDiffOld) / 2;
            yTop -= (int) yAdd;
        }


        //no images greater than picture of map
        if (xDiff > width) {
            xDiff = width;
        }
        if (yDiff > height) {
            yDiff = height;
        }

        //no images outside of picture of map
        if (xLeft < 0) {
            xLeft = 0;
        }
        if (xLeft + xDiff > 1024) {
            xLeft = 1024 - xDiff;
        }
        if (yTop < 0) {
            yTop = 0;
        }
        if (yTop + yDiff > 768) {
            yTop = 768 - yDiff;
        }

        //create subimage and return upscaled one
        BufferedImage subimage = frameBuffer.getSubimage((int) xLeft, (int) yTop, (int) xDiff, (int) yDiff);
        //@todo replace with faster scaling method
        return subimage.getScaledInstance(1024, 768, Image.SCALE_FAST);
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        for (int i = 0; i < keys_player_1.length; i++) {
            if (key == keys_player_1[i] && !booleans_player1[i]) {
                player1.changeMovement(actions[i]);
                booleans_player1[i] = true;
            }
            if (key == keys_player_2[i] && !booleans_player2[i]) {
                player2.changeMovement(actions[i]);
                booleans_player2[i] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        for (int i = 0; i < keys_player_1.length; i++) {
            if (key == keys_player_1[i] && booleans_player1[i]) {
                booleans_player1[i] = false;
            }
            if (key == keys_player_2[i] && booleans_player2[i]) {
                booleans_player2[i] = false;
            }
        }
        if (!booleans_player1[1] && !booleans_player1[2]) {
            player1.changeMovement(Player.Movement.STOP_MOVING);
        }
        if (!booleans_player2[1] && !booleans_player2[2]) {
            player2.changeMovement(Player.Movement.STOP_MOVING);
        }
        //player1 change direction if both keys were pressed and now one was released
        if (!booleans_player1[1] && booleans_player1[2]) {
            player1.changeMovement(actions[2]);
        } else if (booleans_player1[1] && !booleans_player1[2]) {
            player1.changeMovement(actions[1]);
        }
        //player2 change direction if both keys were pressed and now one was released
        if (!booleans_player2[1] && booleans_player2[2]) {
            player2.changeMovement(actions[2]);
        } else if (booleans_player2[1] && !booleans_player2[2]) {
            player2.changeMovement(actions[1]);
        }
    }

}
