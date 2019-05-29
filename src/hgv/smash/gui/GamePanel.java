package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.GameloopThread;
import hgv.smash.game.LevelMap;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends Panel implements KeyEventDispatcher, KeyListener {

    private static final int FRAMERATE = 60;
    private static long JUMP_COOLDOWN = 2000;
    private boolean running;
    private int currentFramerate;
    private Player player1;
    private Player player2;
    private LevelMap levelMap;
    private BufferedImage frameBuffer;

    public GamePanel(Avatar a1, Avatar a2, LevelMap map) {
        // assign and create params
        running = true;
        GameloopThread gameloopThread = new GameloopThread(this);
        player1 = new Player(a1, 200);
        player2 = new Player(a2, 300);
        levelMap = map;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        addKeyListener(this);

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

            player1.calc(timedelta);
            player2.calc(timedelta);
            levelMap.calc(timedelta);

            // collision detection
            player1.collide(levelMap);
            player2.collide(levelMap);

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

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // paint buffer
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(frameBuffer, 0, 0, this);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        processKeyEvent(keyEvent);
        return true;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        switch (key) {
            case 'w': {
                player1.jump();
                break;
            }
            case 'i': {
                player2.jump();
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        switch (key) {
            case 'a': {
                player1.walkLeft();
                break;
            }
            case 'd': {
                player1.walkRight();
                break;
            }
            case 'j': {
                player2.walkLeft();
                break;
            }
            case 'l': {
                player2.walkRight();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        if (key == 'a' || key == 'd') {
            player1.stay();
        } else if (key == 'j' || key == 'l') {
            player2.stay();
        }
    }
}
