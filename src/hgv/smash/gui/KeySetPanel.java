package hgv.smash.gui;


import hgv.smash.resources.Design;
import hgv.smash.resources.KeyBoardLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class KeySetPanel extends Panel implements MouseListener {

    private static final String PATH = "./resources/keyboard/keyboardLayout.ser";



    private char[] player1Keys;
    private char[] player2Keys;

    private JLabel[] player1Label;
    private JLabel[] player2Label;
    private JButton saveButton;
    private JButton restoreDefaultButton;

    private final Border loweredBorder = BorderFactory.createLoweredBevelBorder();
    private final Border raisedBorder = BorderFactory.createRaisedBevelBorder();

    private static final int[][] KEY_POSITION = {{1, 0},{0, 1}, {2, 1},{3, 1}, {3, 0}};

    private JLabel selectedKey;

    private KeyBoardLayout keyBoardLayout = null;

    private Component originalContentPane;


    public KeySetPanel() {
        this.keyBoardLayout = getKeyBoardLayoutFromFile();
        player1Keys = keyBoardLayout.getPlayer1Keys();
        player2Keys = keyBoardLayout.getPlayer2Keys();

        JPanel wholePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel keySetPanel = new JPanel();
        JPanel leftPlayerKeySet = new JPanel();
        JPanel rightPlayerKeySet = new JPanel();

        wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        keySetPanel.setLayout(new BoxLayout(keySetPanel, BoxLayout.LINE_AXIS));
        leftPlayerKeySet.setLayout(new GridBagLayout());
        rightPlayerKeySet.setLayout(new GridBagLayout());

        player1Label = new JLabel[5];
        player2Label = new JLabel[5];

        for (int i = 0; i < player1Keys.length; i++) {
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = KEY_POSITION[i][0];
            gbc1.gridy = KEY_POSITION[i][1];
            gbc1.ipadx = 60;
            gbc1.ipady = 60;
            gbc1.insets = new Insets(5, 5, 5, 5);
            player1Label[i] = new JLabel(Character.toString(Character.toUpperCase(player1Keys[i])));
            player1Label[i].setFont(Design.getDefaultFont(20));
            player1Label[i].setHorizontalAlignment(JLabel.CENTER);
            player1Label[i].addMouseListener(this);
            player1Label[i].setPreferredSize(new Dimension(40, 40));
            player1Label[i].setBorder(raisedBorder);
            leftPlayerKeySet.add(player1Label[i], gbc1);


            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = KEY_POSITION[i][0];
            gbc2.gridy = KEY_POSITION[i][1];
            gbc2.ipadx = 60;
            gbc2.ipady = 60;
            gbc2.insets = new Insets(5, 5, 5, 5);
            player2Label[i] = new JLabel(Character.toString(Character.toUpperCase(player2Keys[i])));
            player2Label[i].setFont(Design.getDefaultFont(20));
            player2Label[i].setHorizontalAlignment(JLabel.CENTER);
            player2Label[i].addMouseListener(this);
            player2Label[i].setPreferredSize(new Dimension(40, 40));
            player2Label[i].setBorder(raisedBorder);
            rightPlayerKeySet.add(player2Label[i], gbc2);
        }

        keySetPanel.add(leftPlayerKeySet);
        keySetPanel.add(rightPlayerKeySet);
        wholePanel.add(Box.createVerticalStrut(190));
        wholePanel.add(keySetPanel);

        saveButton = new JButton("Speichern");
        saveButton.setFont(Design.getDefaultFont(20));
        saveButton.addMouseListener(this);
        buttonPanel.add(saveButton);

        buttonPanel.add(Box.createHorizontalStrut(10));
        restoreDefaultButton = new JButton("Standard Belegung wiederherstellen");
        restoreDefaultButton.setFont(Design.getDefaultFont(20));
        restoreDefaultButton.addMouseListener(this);
        buttonPanel.add(restoreDefaultButton);

        wholePanel.add(Box.createVerticalStrut(50));
        wholePanel.add(buttonPanel);

        add(wholePanel);

        OurKeyListener.getInstance().setPanel(this);
        updateUI();


    }

    public static KeyBoardLayout getKeyBoardLayoutFromFile() {
        KeyBoardLayout keyBoardLayout = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            File file = new File(PATH);
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            if(object instanceof KeyBoardLayout){
                keyBoardLayout=(KeyBoardLayout)object;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (Exception oOSException) {
                    oOSException.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception fOSException) {
                    fOSException.printStackTrace();
                }
            }
        }
        if (keyBoardLayout == null) {
            keyBoardLayout = new KeyBoardLayout();
        }
        return keyBoardLayout;
    }

    public void setOriganalContentPane(Component component) {
        originalContentPane = component;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if (selectedKey != null) {
            for (int i = 0; i < player1Label.length; i++) {
                if (player1Label[i].equals(selectedKey)) {
                    player1Keys[i] = Character.toUpperCase(e.getKeyChar());
                }
                if (player2Label[i].equals(selectedKey)) {
                    player2Keys[i] = Character.toUpperCase(e.getKeyChar());
                }
            }
            selectedKey.setText(Character.toString(Character.toUpperCase(e.getKeyChar())));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            for (int i = 0; i < player1Label.length; i++) {
                player1Label[i].setBorder(raisedBorder);
                player2Label[i].setBorder(raisedBorder);
            }
            Object source = e.getSource();
            System.out.println(player1Label[0].toString());
            for (int i = 0; i < player1Label.length; i++) {
                if (source.equals(player1Label[i])) {
                    selectedKey = player1Label[i];
                }
                if (source.equals(player2Label[i])) {
                    selectedKey = player2Label[i];
                }
            }
            selectedKey.setBorder(loweredBorder);
        } else if (e.getSource() instanceof JButton) {
            if (e.getSource().equals(saveButton)) {
                keyBoardLayout.setKeysForPlayer1(player1Keys);
                keyBoardLayout.setKeysForPlayer2(player2Keys);
                keyBoardLayout.serialize();
                Frame.getInstance().getContentPane().removeAll();
                Frame.getInstance().getContentPane().add(originalContentPane);
                if(originalContentPane instanceof GamePanel){
                    ((GamePanel)originalContentPane).updateKeys();
                    ((GamePanel)originalContentPane).setPanelActive();
                }
                ((JPanel) Frame.getInstance().getContentPane()).updateUI();
            } else if (e.getSource().equals(restoreDefaultButton)) {
                player1Keys = KeyBoardLayout.getPlayer1DefaultKeys();
                player2Keys = KeyBoardLayout.getPlayer2DefaultKeys();
                if (selectedKey != null) {
                    selectedKey.setBorder(raisedBorder);
                    selectedKey = null;
                }
                for (int i = 0; i < player1Label.length; i++) {
                    player1Label[i].setText(Character.toString(Character.toUpperCase(player1Keys[i])));
                    player2Label[i].setText(Character.toString(Character.toUpperCase(player2Keys[i])));
                }
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
