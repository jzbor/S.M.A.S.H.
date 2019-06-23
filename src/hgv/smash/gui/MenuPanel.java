package hgv.smash.gui;

import hgv.smash.exceptions.AvatarNotAvailableException;
import hgv.smash.game.LevelMap;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Design;
import hgv.smash.resources.Music;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//Klasse für das Start- und Hauptmenü:

public class MenuPanel extends Panel implements ActionListener {
    private JComboBox buttonPlayer1, buttonPlayer2, buttonMap;
    private BufferedImage backgroundImage;
    private BufferedImage transparentBackground;
    private BufferedImage previewPlayer1;
    private BufferedImage previewPlayer2;
    private BufferedImage platformImage;
    private JButton startButton;
    private JButton disclaimerButton;
    private JFrame frame;
    private Avatar avatar1;
    private Avatar avatar2;
    private LevelMap levelMap;
    private BufferedImage origBackImg;

    public MenuPanel() {
        frame = Frame.getInstance();
        Frame.getInstance().currentpanel(0);
        if (Frame.getInstance().getMusic()) {
            Music oldMusic = Music.getInstanceScoreMusicSowjet();
            oldMusic.stop();
            oldMusic = Music.getInstanceScoreMusicBavaria();
            oldMusic.stop();
            oldMusic = Music.getInstanceScoreMusicScout();
            oldMusic.stop();
            Music newMusic = Music.getInstanceMenuMusic();
            newMusic.play();
        }
        setLayout(null);
        //Einfuegen und Deklarieren der Hintergrundgraphik
        try {
            backgroundImage = ImageIO.read(new File("./resources/menu/background.png"));
            origBackImg = backgroundImage;
            transparentBackground = ImageIO.read(new File("./resources/menu/background_logo.png"));
            platformImage = ImageIO.read(new File("./resources/menu/platform.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //repaint();

        //Avatar_Names ist ein statisches Feld und ein public Attribut der Klasse Avatar.
        buttonPlayer1 = new JComboBox(Avatar.AVATAR_NAMES);
        buttonPlayer2 = new JComboBox(Avatar.AVATAR_NAMES);
        //Level_Map ist ein statisches Feld und ein public Attribut der LevelMap
        buttonMap = new JComboBox(LevelMap.MAP_NAMES);
        buttonPlayer1.addActionListener(this);
        buttonPlayer2.addActionListener(this);
        buttonMap.addActionListener(this);

        buttonPlayer1.setBounds(1024 / 4 - 100, 280, 200, 50);
        buttonPlayer2.setBounds(1024 / 4 * 3 - 100, 280, 200, 50);
        buttonMap.setBounds(1024 / 2 - 75, 280, 150, 50);
        buttonPlayer1.setRenderer(new MyComboBoxRenderer("Ersten Avatar wählen"));
        buttonPlayer1.setSelectedIndex(-1);
        buttonPlayer2.setRenderer(new MyComboBoxRenderer("Zweiten Avatar wählen"));
        buttonPlayer2.setSelectedIndex(-1);
        buttonMap.setRenderer(new MyComboBoxRenderer("Map auswählen"));
        buttonMap.setSelectedIndex(-1);

        add(buttonPlayer1);
        add(buttonPlayer2);
        add(buttonMap);

        startButton = new JButton("Lass uns boxen");
        startButton.setBounds(412, 400, 200, 50);
        startButton.setBackground(Color.WHITE);
        startButton.addActionListener(this);
        add(startButton);

        disclaimerButton = new JButton("Dementi");
        disclaimerButton.setBounds(412 - 50, 600, 300, 50);
        disclaimerButton.setBackground(Color.WHITE);
        disclaimerButton.addActionListener(this);
        add(disclaimerButton);

        // Add fonts
        buttonMap.setFont(Design.getDefaultFont());
        buttonPlayer1.setFont(Design.getDefaultFont());
        buttonPlayer2.setFont(Design.getDefaultFont());
        startButton.setFont(Design.getDefaultFont(20));
        startButton.setBackground(Design.getPrimaryColor());
        startButton.setForeground(Design.getSecondaryColor());
        disclaimerButton.setFont(Design.getDefaultFont());
        disclaimerButton.setBackground(Design.getPrimaryColor());
        disclaimerButton.setForeground(Design.getSecondaryColor());

        buttonMap.setBackground(Design.getPrimaryColor());
        buttonMap.setForeground(Design.getSecondaryColor());
        buttonPlayer1.setBackground(Design.getPrimaryColor());
        buttonPlayer1.setForeground(Design.getSecondaryColor());
        buttonPlayer2.setBackground(Design.getPrimaryColor());
        buttonPlayer2.setForeground(Design.getSecondaryColor());

        previewPlayer1 = null;
        previewPlayer2 = null;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonPlayer1) {
            String s = (String) buttonPlayer1.getSelectedItem();
            System.out.println(s);
            int variable = -1;
            for (int i = 0; i < Avatar.AVATAR_NAMES.length; i++) {
                if (Avatar.AVATAR_NAMES[i].equals(s)) {
                    variable = i;
                }
            }
            if (variable > -1) {
                try {
                    avatar1 = new Avatar(variable);
                } catch (AvatarNotAvailableException e) {
                    e.printStackTrace();
                }
                previewPlayer1 = avatar1.getImage(Avatar.NORMAL);
                repaint();
            }
        } else if (actionEvent.getSource() == buttonPlayer2) {
            String s = (String) buttonPlayer2.getSelectedItem();
            int variable = -1;
            for (int i = 0; i < Avatar.AVATAR_NAMES.length; i++) {
                if (Avatar.AVATAR_NAMES[i].equals(s)) {
                    variable = i;
                }
            }
            if (variable > -1) {
                try {
                    avatar2 = new Avatar(variable);
                } catch (AvatarNotAvailableException e) {
                    e.printStackTrace();
                }
                previewPlayer2 = avatar2.getImage(Avatar.NORMAL);
                repaint();
            }
        } else if (actionEvent.getSource() == buttonMap) {
            buttonMap.setBackground(Design.getPrimaryColor());
            String s = (String) buttonMap.getSelectedItem();
            int variable = -1;
            for (int i = 0; i < LevelMap.MAP_NAMES.length; i++) {
                if (LevelMap.MAP_NAMES[i].equals(s)) {
                    variable = i;
                }
            }
            if (variable > -1) {
                levelMap = null;
                try {
                    levelMap = LevelMap.load(LevelMap.MAP_NAMES[variable]);
                    backgroundImage = levelMap.getBackgroundImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                repaint();
            }
        } else if (actionEvent.getSource() == startButton) {
            if (avatar1 != null && avatar2 != null && levelMap != null) {
                if (Frame.getInstance().getMusic()) {
                    Music oldMusic = Music.getInstanceMenuMusic();
                    oldMusic.stop();
                    Music newMusic = Music.getInstanceGameMusic();
                    newMusic.play();
                }
                Frame.getInstance().currentpanel(1);
                GamePanel gamePanel = new GamePanel(avatar1, avatar2, levelMap);
                frame = Frame.getInstance();
                System.out.println(frame);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(gamePanel);
                ((JPanel) (frame.getContentPane())).updateUI();
            } else {
                JOptionPane.showMessageDialog(this, "Ihr müsst euch erst Avatare aussuchen und auf eine Karte einigen, um zu spielen.");
            }
        } else if (actionEvent.getSource() == disclaimerButton) {
            File currentFile = new File("./resources/disclaimer/disclaimer.txt");
            StringBuilder disclaimer = new StringBuilder();
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(currentFile));
                int i;
                while ((i = isr.read()) != -1) {
                    disclaimer.append((char) i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            new TextDialog(disclaimer.toString(), (Frame) SwingUtilities.getWindowAncestor(this),
                    "Haftungsausschluss", new Dimension(700, 400));
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(backgroundImage, 0, 0, null);
        if (backgroundImage != origBackImg) {
            Color color = new Color(152, 19, 43, 150);
            graphics2D.setColor(color);
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
        }
        graphics2D.drawImage(transparentBackground, 0, 0, null);
        graphics2D.drawImage(platformImage, 75, 200, null);
        if (previewPlayer1 != null) {
            graphics2D.drawImage(previewPlayer1, 75 + platformImage.getWidth() / 2 - previewPlayer1.getWidth() / 2,
                    200 - previewPlayer1.getHeight() + 15, null);
        }
        graphics2D.drawImage(platformImage, 949 - platformImage.getWidth(), 200, null);
        if (previewPlayer2 != null) {
            graphics2D.drawImage(previewPlayer2, 949 - platformImage.getWidth() + platformImage.getWidth() / 2 - previewPlayer2.getWidth() / 2,
                    200 - previewPlayer2.getHeight() + 15, null);
        }
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
