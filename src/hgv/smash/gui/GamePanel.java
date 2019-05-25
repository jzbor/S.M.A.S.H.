package hgv.smash.gui;

import hgv.smash.game.GameloopThread;
import hgv.smash.game.LevelMap;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends Panel implements KeyListener {

    private boolean running;
    private long lastFrame;
    private GameloopThread gameloopThread;
    private Player player1;
    private Player player2;
    private LevelMap levelMap;

    public GamePanel(Avatar a1, Avatar a2, LevelMap map) {
        // assign and create params
        running = true;
        gameloopThread = new GameloopThread(this);
        player1 = new Player(a1);
        player2 = new Player(a2);
        levelMap = map;


    // start
        gameloopThread.start();
}

    public void gameloop(){
        lastFrame = System.currentTimeMillis();
        while (running){
            long timedelta = System.currentTimeMillis() - lastFrame;
            lastFrame = System.currentTimeMillis();

            player1.calc(timedelta);
            player2.calc(timedelta);
            levelMap.calc(timedelta);

            player1.collide(levelMap);
            player2.collide(levelMap);

            updateUI();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D)graphics;

        player1.draw(graphics2D);
        player2.draw(graphics2D);
        levelMap.draw(graphics2D);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
