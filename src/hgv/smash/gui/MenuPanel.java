package hgv.smash.gui;

import hgv.smash.game.LevelMap;
import hgv.smash.resources.Avatar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Klasse für das Start- und Hauptmenü:

public class MenuPanel extends Panel implements ActionListener {
    private JComboBox player1, player2, map;

    public MenuPanel (){
        //Avatar_Names ist ein statisches Feld und ein public Attribut der Klasse Avatar.
        player1=new JComboBox(Avatar.AVATAR_NAMES);
        player2=new JComboBox(Avatar.AVATAR_NAMES);
        //Level_Map ist ein statisches Feld und ein public Attribut der LevelMap
        map=new JComboBox(LevelMap.MAP_NAMES);
        player1.addActionListener(this);
        player2.addActionListener(this);
        map.addActionListener(this );
        GridLayout menuLayout= new GridLayout( 4,2);
        this.setLayout(menuLayout);

    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
