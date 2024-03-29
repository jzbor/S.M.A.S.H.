package hgv.smash.gui;

import hgv.smash.resources.Design;
import hgv.smash.resources.Music;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private static final String TITLE = "S.M.A.S.H.";
    private static final Dimension FINAL_SIZE = new Dimension(1024, 768);
    private static final Frame ourInstance = new Frame();
    private boolean playMusic;
    private int currentPanel;
    private boolean playSound;

    // 0=Menu; 1=Game; 2= Score;
    private Frame() {
        super(TITLE);
        this.setResizable(false);
        playMusic = true;
        playSound = true;
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
        JMenu fileMenu = new JMenu("Programm");
        JMenu prefMenu = new JMenu("Einstellungen");
        JMenu gameMenu = new JMenu("Spiel");
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
        JMenuItem quitItem = new JMenuItem("Beenden");
        fileMenu.add(quitItem);
        quitItem.addActionListener(e -> System.exit(0));
    }

    private void addGameMenuItems(JMenu gameMenu) {
        JMenuItem pauseItem = new JMenuItem("Spiel pausieren");
        gameMenu.add(pauseItem);
        JMenuItem cameraItem = new JMenuItem("Kamera an/aus");
        gameMenu.add(cameraItem);
        pauseItem.addActionListener(e -> {
            Panel comp = (Panel) getContentPane().getComponents()[0];
            if (comp instanceof GamePanel) {
                GamePanel gamePanel = (GamePanel) comp;
                gamePanel.pause();
            }
        });
        cameraItem.addActionListener(e -> {
            Panel comp = (Panel) getContentPane().getComponents()[0];
            if (comp instanceof GamePanel) {
                GamePanel gamePanel = (GamePanel) comp;
                gamePanel.changeCameraRunning();
            }
        });

    }

    private void addPrefMenuItems(JMenu prefMenu) {

        JMenuItem musicItem = new JMenuItem("Musik an/aus");
        prefMenu.add(musicItem);
        JMenuItem prefItem = new JMenuItem("Tastenbelegung");
        prefMenu.add(prefItem);
        JMenuItem soundItem = new JMenuItem("Geräusche an/aus");
        prefMenu.add(soundItem);
        soundItem.addActionListener(e -> playSound = !playSound);
        prefItem.addActionListener(e -> {
            KeySetPanel keySetPanel = new KeySetPanel();
            keySetPanel.setOriganalContentPane(Frame.getInstance().getContentPane().getComponents()[0]);
            Frame.getInstance().getContentPane().removeAll();
            Frame.getInstance().getContentPane().add(keySetPanel);
            ((JPanel) Frame.getInstance().getContentPane()).updateUI();
        });
        musicItem.addActionListener(e -> {
            Music menumusic = Music.getInstanceMenuMusic();
            Music gamemusic = Music.getInstanceGameMusic();
            Music scoremusicSowjet = Music.getInstanceScoreMusicSowjet();
            Music scoremusicBavaria = Music.getInstanceScoreMusicBavaria();
            if (playMusic) {
                menumusic.stop();
                gamemusic.stop();
                scoremusicSowjet.stop();
                scoremusicBavaria.stop();
                playMusic = false;
            } else {
                if (currentPanel == 0) {
                    menumusic.play();
                }
                if (currentPanel == 1) {
                    gamemusic.play();
                }
                if (currentPanel == 2) {
                    scoremusicSowjet.play();

                }
                playMusic = true;
            }


        });
    }

    public void currentpanel(int i) {
        currentPanel = i;
    }

    public boolean getMusic() {
        return playMusic;
    }

    public boolean getSound() {
        return playSound;
    }
}
