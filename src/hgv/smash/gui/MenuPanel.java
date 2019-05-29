package hgv.smash.gui;

import hgv.smash.game.LevelMap;
import hgv.smash.resources.Avatar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Klasse für das Start- und Hauptmenü:

public class MenuPanel extends Panel implements ActionListener {
    private JComboBox buttonPlayer1, buttonPlayer2, buttonMap;
    private BufferedImage backgroundImage;
    private BufferedImage previewPlayer1;
    private BufferedImage previewPlayer2;

    public MenuPanel () {
        setLayout(null);
        //Einfuegen und Deklarieren der Hintergrundgraphik
        try {
            backgroundImage = ImageIO.read(new File("./resources/HintergrundMenü/neu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();

        //Avatar_Names ist ein statisches Feld und ein public Attribut der Klasse Avatar.
        buttonPlayer1 = new JComboBox(Avatar.AVATAR_NAMES);
        buttonPlayer2 = new JComboBox(Avatar.AVATAR_NAMES);
        //Level_Map ist ein statisches Feld und ein public Attribut der LevelMap
        buttonMap = new JComboBox(LevelMap.MAP_NAMES);
        buttonPlayer1.addActionListener(this);
        buttonPlayer2.addActionListener(this);
        buttonMap.addActionListener(this);

        buttonPlayer1.setBounds(362,150,150,100);
        buttonPlayer2.setBounds(362,84+100+150,150,100);
        buttonMap.setBounds(362,84+100+84+100+150,150,100);
        add(buttonPlayer1);
        add(buttonPlayer2);
        add (buttonMap);

        previewPlayer1=null;
        previewPlayer2=null;


    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonPlayer1) {
            String s = (String) buttonPlayer1.getSelectedItem();//get the selected item
            int variable = -1;
            for (int i = 0; i < Avatar.AVATAR_NAMES.length; i++) {
                if (Avatar.AVATAR_NAMES[i] == s) {
                    variable = i;
                }
            }
            switch (variable) {//check for a match
                case 0:
                    try {
                        previewPlayer1 = ImageIO.read(new File(Avatar.AVATAR_PATH + Avatar.AVATAR_FILES[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    break;
                case 1:

                    break;
                default:
                    System.err.println("Avatar nicht existent");
                    break;
            }
        }
        else if (actionEvent.getSource()==buttonPlayer2) {
            String s = (String) buttonPlayer1.getSelectedItem();//get the selected item
            int variable = -1;
            for (int i = 0; i < Avatar.AVATAR_NAMES.length; i++) {
                if (Avatar.AVATAR_NAMES[i] == s) {
                    variable = i;
                }
            }
            switch (variable) {//check for a match
                case 0:
                    try {
                        previewPlayer2 = ImageIO.read(new File(Avatar.AVATAR_PATH + Avatar.AVATAR_FILES[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repaint();
                    break;
                case 1:

                    break;
                default:
                    System.err.println("Avatar nicht existent");
                    break;
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D=(Graphics2D)g;
        graphics2D.drawImage(backgroundImage, 0, 0, null);
        graphics2D.drawImage(previewPlayer1, 200, 280, null);
        graphics2D.drawImage(previewPlayer2, 700,280, null);

    }

}
