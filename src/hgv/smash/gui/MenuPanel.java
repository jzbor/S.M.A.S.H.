package hgv.smash.gui;

import hgv.smash.game.LevelMap;
import hgv.smash.resources.Avatar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Klasse für das Start- und Hauptmenü:

public class MenuPanel extends Panel implements ActionListener {
    private JComboBox buttonPlayer1, buttonPlayer2, buttonMap;
    private BufferedImage backgroundImage;
    private BufferedImage previewPlayer1;
    private BufferedImage previewPlayer2;
    private JButton startButton;
    private JLabel labelGameTitel;
    private Frame frame;

    public MenuPanel() {
        frame = Frame.getInstance();
        setLayout(null);
        //Einfuegen und Deklarieren der Hintergrundgraphik
        try {
            backgroundImage = ImageIO.read(new File("./resources/HintergrundMenü/neu.png"));
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

        buttonPlayer1.setBounds(1024 / 4 - 75, 230, 150, 50);
        buttonPlayer2.setBounds(1024 / 4 * 3 - 75, 230, 150, 50);
        buttonMap.setBounds(1024 / 2 - 75, 230, 150, 50);
        buttonPlayer1.setRenderer(new MyComboBoxRenderer("Choose Avatar one"));
        buttonPlayer1.setSelectedIndex(-1);
        buttonPlayer2.setRenderer(new MyComboBoxRenderer("Choose Avatar two"));
        buttonPlayer2.setSelectedIndex(-1);
        buttonMap.setRenderer(new MyComboBoxRenderer("Choose your map"));
        buttonMap.setSelectedIndex(-1);

        buttonPlayer1.setBackground(Color.WHITE);
        buttonPlayer2.setBackground(Color.WHITE);
        buttonMap.setBackground(Color.WHITE);

        add(buttonPlayer1);
        add(buttonPlayer2);
        add(buttonMap);

        startButton = new JButton("Let's Fight");
        startButton.setBounds(462, 400, 100, 50);
        startButton.setBackground(Color.WHITE);
        startButton.addActionListener(this);
        add(startButton);

        labelGameTitel = new JLabel("S.M.A.S.H");
        labelGameTitel.setBounds(15, -70, 400, 200);
        labelGameTitel.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
        add(labelGameTitel);

        previewPlayer1 = null;
        previewPlayer2 = null;


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonPlayer1) {
            String s = (String) buttonPlayer1.getSelectedItem();
            int variable = -1;
            for (int i = 0; i < Avatar.AVATAR_NAMES.length; i++) {
                if (Avatar.AVATAR_NAMES[i].equals(s)) {
                    variable = i;
                }
            }
            switch (variable) {
                case 0:
                    try {
                        previewPlayer1 = ImageIO.read(new File(Avatar.AVATAR_PATH + Avatar.FILENAMES[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    break;
                case 1:
                    try {
                        previewPlayer1 = ImageIO.read(new File(Avatar.AVATAR_PATH + Avatar.FILENAMES[1]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    break;
                default:
                    System.err.println("Avatar nicht existent");
                    break;
            }
        } else if (actionEvent.getSource() == buttonPlayer2) {
            String s = (String) buttonPlayer2.getSelectedItem();
            int variable = -1;
            for (int i = 0; i < Avatar.AVATAR_NAMES.length; i++) {
                if (Avatar.AVATAR_NAMES[i] == s) {
                    variable = i;
                }
            }
            switch (variable) {
                case 0:
                    try {
                        previewPlayer2 = ImageIO.read(new File(Avatar.AVATAR_PATH + Avatar.FILENAMES[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    System.out.println("case 0");
                    break;
                case 1:
                    try {
                        previewPlayer2 = ImageIO.read(new File(Avatar.AVATAR_PATH + Avatar.FILENAMES[1]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    System.out.println("case 1");
                    break;
                default:
                    System.err.println("Avatar nicht existent");
                    break;
            }
        } else if (actionEvent.getSource() == buttonMap) {
            String s = (String) buttonMap.getSelectedItem();
            int variable = -1;
            for (int i = 0; i < LevelMap.MAP_NAMES.length; i++) {
                if (LevelMap.MAP_NAMES[i].equals(s)) {
                    variable = i;
                }
            }

            switch (variable) {
                case 0:
                    try {
                        backgroundImage = ImageIO.read(new File((LevelMap.MAP_PATH + LevelMap.MAP_NAMES[0]) + "-bg.jpeg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    break;
                case 1:

                    try {
                        backgroundImage = ImageIO.read(new File((LevelMap.MAP_PATH + LevelMap.MAP_NAMES[1]) + "-bg.jpeg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    break;
                default:
                    System.err.println("Avatar nicht existent");
                    break;
            }
        } else if (actionEvent.getSource() == startButton) {

            LevelMap levelMap = null;
            try {
                levelMap = LevelMap.load(LevelMap.MAP_PATH + LevelMap.MAP_NAMES[0]);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("failed");
            }

            GamePanel gamePanel = new GamePanel(Avatar.debugAvatar(), Avatar.debugAvatar(), levelMap);
            frame.getContentPane().removeAll();
            frame.setContentPane(gamePanel);
            ((JPanel) (frame.getContentPane())).updateUI();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(backgroundImage, 0, 0, null);
        graphics2D.drawImage(previewPlayer1, 210, 320, null);
        graphics2D.drawImage(previewPlayer2, 723, 320, null);

        Ellipse2D cloud = new Ellipse2D.Double(100, 220, 300, 120);
        graphics2D.setColor(new Color(102, 51, 0));

        //graphics2D.fill(cloud);


        graphics2D.setColor(new Color(255, 204, 51));
        Rectangle2D floor = new Rectangle2D.Double(165, 528, 180, 15);
        graphics2D.fill(floor);
        Rectangle2D floor2 = new Rectangle2D.Double(670, 528, 180, 15);
        graphics2D.fill(floor2);
        graphics2D.setColor(Color.YELLOW);
        Rectangle2D throne = new Rectangle2D.Double(690, 508, 140, 20);
        graphics2D.fill(throne);
        Rectangle2D throne2 = new Rectangle2D.Double(185, 508, 140, 20);
        graphics2D.fill(throne2);


    }

}
