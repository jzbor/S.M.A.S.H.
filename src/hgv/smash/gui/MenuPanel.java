package hgv.smash.gui;

import hgv.smash.game.LevelMap;
import hgv.smash.resources.Avatar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

//Klasse für das Start- und Hauptmenü:

public class MenuPanel extends Panel implements ActionListener {
    private JComboBox buttonPlayer1, buttonPlayer2, buttonMap;
    private JLabel backround;

    public MenuPanel () {
        setLayout(null);
        backround=new JLabel (new ImageIcon("./recources/HintergrundMenü/redStar.png") );
        backround.setBounds(200,200,1024,768);
        add(backround);

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


    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
