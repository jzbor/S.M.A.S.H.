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

public class GamePanel extends Panel implements KeyEventDispatcher, KeyListener {

    private static final int FRAMERATE = 60;
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
    private int[] actions = {GamePanel.Movement.JUMP, GamePanel.Movement.MOVE_LEFT, GamePanel.Movement.MOVE_RIGHT, Movement.NORMAL_HIT};
    //last performed action by pressing
    private int lastChangePlayer1 = Movement.STOP_MOVING;
    private int lastChangePlayer2 = Movement.STOP_MOVING;
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
        player1 = new Player(a1, 200, map);
        player2 = new Player(a2, 300, map);
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
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

            Music.getInstance().stop();
            running = false;
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
        //problem: long typed Keys solved by using keyPressed and keyReleased
        /*
        char key = keyEvent.getKeyChar();

        switch (key) {
            case 'w': {
                player1.changeMovement(Movement.JUMP);
                break;
            }
            case 'i': {
                player2.changeMovement(Movement.JUMP);
                break;
            }
        }
*/
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
            player1.changeMovement(GamePanel.Movement.STOP_MOVING);
        }
        if (!booleans_player2[1] && !booleans_player2[2]) {
            player2.changeMovement(GamePanel.Movement.STOP_MOVING);
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

    public static class Movement {
        public static final int MOVE_LEFT = -1;
        public static final int MOVE_RIGHT = 1;
        public static final int STOP_MOVING = 0;
        public static final int JUMP = 2;
        public static final int NORMAL_HIT = 3;
    }
}
