package hgv.smash.game;

import hgv.smash.gui.GamePanel;

public class GameloopThread extends Thread {

    private GamePanel panel;

    public GameloopThread(GamePanel gamePanel){
        super("Gameloop");
        this.panel = gamePanel;
    }

    @Override
    public void run() {
        super.run();
        panel.gameloop();
    }
}
