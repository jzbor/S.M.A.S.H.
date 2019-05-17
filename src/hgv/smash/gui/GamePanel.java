package hgv.smash.gui;

import hgv.smash.game.GameloopThread;
import hgv.smash.game.LevelMap;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends Panel implements KeyListener {

    private GameloopThread gameloop;
    private Player player1;
    private Player player2;
    private LevelMap levelMap;

    public GamePanel(Avatar a1, Avatar a2, LevelMap map) {

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
