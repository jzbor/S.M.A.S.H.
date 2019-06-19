package hgv.smash.gui;

import hgv.smash.resources.Design;
import hgv.smash.resources.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {

    private static final String TITLE = "S.M.A.S.H.";
    private static final Dimension FINAL_SIZE = new Dimension(1024, 768);
    private static final Dimension DEBUG_SIZE = new Dimension(1366, 768);
    private static final Frame ourInstance = new Frame();
    private boolean playMusic;
    private int currentPanel;

    // 0=Menu; 1=Game; 2= Score;
    private Frame() {
        super(TITLE);
        playMusic = true;
        createMenu();
        // Init Frame
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(FINAL_SIZE);


        System.out.println("Frame: " + this);

    }

    public static Frame getInstance() {
        return ourInstance;
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenu prefMenu = new JMenu("Preferences");
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(prefMenu);
        addFileMenuItems(fileMenu);
        addPrefMenuItems(prefMenu);
        addGameMenuItems(gameMenu);

        menuBar.setBackground(Design.getPrimaryColor());
        fileMenu.setBackground(Design.getPrimaryColor());
        prefMenu.setBackground(Design.getPrimaryColor());
        gameMenu.setBackground(Design.getPrimaryColor());
        setDesign(menuBar, Design.getPrimaryColor(), Design.getSecondaryColor(), Design.getDefaultFont());
    }

    private void setDesign(JMenuBar menuBar, Color backColor, Color foreColor, Font font) {
        menuBar.setBackground(backColor);
        menuBar.setFont(font);
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            menu.setFont(font);
            menu.setBackground(backColor);
            menu.setForeground(foreColor);
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                item.setFont(font);
                item.setBackground(backColor);
                item.setForeground(foreColor);
            }
        }
    }

    private void addFileMenuItems(JMenu fileMenu) {
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void addGameMenuItems(JMenu gameMenu) {
        JMenuItem pauseItem = new JMenuItem("Pause Game");
        gameMenu.add(pauseItem);
        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel comp = (Panel) getContentPane().getComponents()[0];
                if (comp instanceof GamePanel) {
                    GamePanel gamePanel = (GamePanel) comp;
                    gamePanel.pause();
                }
            }
        });

    }

    private void addPrefMenuItems(JMenu prefMenu) {

        JMenuItem musicItem = new JMenuItem("Musik");
        prefMenu.add(musicItem);
        JMenuItem prefItem = new JMenuItem("Keyboard Preferences");
        prefMenu.add(prefItem);
        prefItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        musicItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Music menumusic = Music.getInstanceMenuMusic();
                Music gamemusic = Music.getInstanceGameMusic();
                Music scoremusic = Music.getInstanceScoreMusic();
                if (playMusic) {
                    menumusic.stop();
                    gamemusic.stop();
                    scoremusic.stop();
                    playMusic = false;
                } else {
                    if (currentPanel == 0) {
                        menumusic.play();
                    }
                    if (currentPanel == 1) {
                        gamemusic.play();
                    }
                    if (currentPanel == 2) {
                        scoremusic.play();

                    }
                    playMusic = true;
                }


            }
        });
    }

    public void currentpanel(int i) {
        currentPanel = i;
    }

    public boolean getMusic() {
        return playMusic;
    }
}
