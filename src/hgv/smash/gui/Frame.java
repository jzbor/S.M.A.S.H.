package hgv.smash.gui;

import hgv.smash.resources.Design;
import hgv.smash.resources.Music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {

    private static final String TITLE = "S.M.A.S.H.";
    private static final Dimension FINAL_SIZE = new Dimension(1024, 768 + 53);
    private static final Dimension DEBUG_SIZE = new Dimension(1366, 768);
    private static final String KEYBOARD_LAYOUT_FILE="./resources/keyboard/keyboardLayout.ser";
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
        JMenuItem cameraItem = new JMenuItem("Kamera an/aus");
        gameMenu.add(cameraItem);
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
        cameraItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel comp = (Panel) getContentPane().getComponents()[0];
                if (comp instanceof GamePanel) {
                    GamePanel gamePanel = (GamePanel) comp;
                    gamePanel.changeCameraRunning();
                }
            }
        });

    }

    private void addPrefMenuItems(JMenu prefMenu) {

        JMenuItem musicItem = new JMenuItem("Musik");
        prefMenu.add(musicItem);
        JMenuItem prefItem = new JMenuItem("Keyboard Preferences");
        prefMenu.add(prefItem);
        JMenuItem soundItem = new JMenuItem("Sound");
        prefMenu.add(soundItem);
        soundItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound = !playSound;
            }
        });
        prefItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KeySetPanel keySetPanel=new KeySetPanel();
                keySetPanel.setOriganalContentPane(Frame.getInstance().getContentPane().getComponents()[0]);
                Frame.getInstance().getContentPane().removeAll();
                Frame.getInstance().getContentPane().add(keySetPanel);
                ((JPanel) Frame.getInstance().getContentPane()).updateUI();
                //@todo set number for frame ZBORIL??
            }
        });
        musicItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
