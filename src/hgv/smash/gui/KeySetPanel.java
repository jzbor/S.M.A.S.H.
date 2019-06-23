package hgv.smash.gui;


import hgv.smash.resources.Design;
import hgv.smash.resources.KeyBoardLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

//import sun.security.krb5.internal.crypto.Des;

public class KeySetPanel extends Panel implements MouseListener {

    private static final String PATH = "./resources/keyboard/keyboardLayout.ser";


    private char[] player1Keys;
    private char[] player2Keys;

    private JLabel[] player1KeyLabels;
    private JLabel[] player2KeyLabels;
    private JButton saveButton;
    private JButton restoreDefaultButton;
    private JButton abortButton;

    private final Border loweredBorder = BorderFactory.createLoweredBevelBorder();
    private final Border raisedBorder = BorderFactory.createRaisedBevelBorder();

    private static final int[][] KEY_POSITION = {{1, 0}, {0, 1}, {2, 1}, {3, 1}, {3, 0}};

    private JLabel selectedKey;

    private KeyBoardLayout keyBoardLayout = null;

    private Component originalContentPane;


    public KeySetPanel() {
        this.keyBoardLayout = getKeyBoardLayoutFromFile();
        player1Keys = keyBoardLayout.getPlayer1Keys();
        player2Keys = keyBoardLayout.getPlayer2Keys();

        JPanel wholePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel leftPlayerKeySet = new JPanel();
        JPanel rightPlayerKeySet = new JPanel();
        JPanel leftPlayerPanel = new JPanel();
        JPanel rightPlayerPanel = new JPanel();

        wholePanel.setLayout(new BorderLayout());
        wholePanel.setBackground(Design.getPrimaryColor());
        wholePanel.setPreferredSize(Frame.getInstance().getContentPane().getSize());

        buttonPanel.setLayout(new GridLayout());
        buttonPanel.setBackground(Design.getPrimaryColor());

        leftPlayerKeySet.setLayout(new GridBagLayout());
        leftPlayerKeySet.setBackground(Design.getPrimaryColor());
        rightPlayerKeySet.setLayout(new GridBagLayout());
        rightPlayerKeySet.setBackground(Design.getPrimaryColor());
        leftPlayerPanel.setLayout(new BorderLayout());
        rightPlayerPanel.setLayout(new BorderLayout());
        player1KeyLabels = new JLabel[5];
        player2KeyLabels = new JLabel[5];

        leftPlayerPanel.setBackground(Design.getPrimaryColor());
        leftPlayerPanel.setForeground(Design.getSecondaryColor());
        rightPlayerPanel.setBackground(Design.getPrimaryColor());
        rightPlayerPanel.setForeground(Design.getSecondaryColor());
        Border paddingBorder = new EmptyBorder(30, 30, 30, 30);
        leftPlayerPanel.setBorder(paddingBorder);
        rightPlayerPanel.setBorder(paddingBorder);

        for (int i = 0; i < player1Keys.length; i++) {
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = KEY_POSITION[i][0];
            gbc1.gridy = KEY_POSITION[i][1];
            gbc1.ipadx = 60;
            gbc1.ipady = 60;
            gbc1.insets = new Insets(5, 5, 5, 5);
            player1KeyLabels[i] = new JLabel(Character.toString(Character.toUpperCase(player1Keys[i])));
            player1KeyLabels[i].setFont(Design.getDefaultFont(20));
            player1KeyLabels[i].setHorizontalAlignment(JLabel.CENTER);
            player1KeyLabels[i].addMouseListener(this);
            player1KeyLabels[i].setPreferredSize(new Dimension(40, 40));
            player1KeyLabels[i].setBorder(raisedBorder);
            leftPlayerKeySet.add(player1KeyLabels[i], gbc1);


            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = KEY_POSITION[i][0];
            gbc2.gridy = KEY_POSITION[i][1];
            gbc2.ipadx = 60;
            gbc2.ipady = 60;
            gbc2.insets = new Insets(5, 5, 5, 5);
            player2KeyLabels[i] = new JLabel(Character.toString(Character.toUpperCase(player2Keys[i])));
            player2KeyLabels[i].setFont(Design.getDefaultFont(20));
            player2KeyLabels[i].setHorizontalAlignment(JLabel.CENTER);
            player2KeyLabels[i].addMouseListener(this);
            player2KeyLabels[i].setPreferredSize(new Dimension(40, 40));
            player2KeyLabels[i].setBorder(raisedBorder);
            rightPlayerKeySet.add(player2KeyLabels[i], gbc2);
        }

        JLabel player1Label = new JLabel("Spieler 1");
        JLabel player2Label = new JLabel("Spieler 2");
        player1Label.setFont(Design.getDefaultFont(50));
        player2Label.setFont(Design.getDefaultFont(50));

        saveButton = new JButton("Speichern");
        saveButton.setFont(Design.getDefaultFont(20));
        saveButton.addMouseListener(this);
        saveButton.setBackground(Design.getPrimaryColor());
        buttonPanel.add(saveButton);
        //   buttonPanel.setPreferredSize(new Dimension(Frame.getInstance().getWidth(),50));


        restoreDefaultButton = new JButton("Wiederherstellen");
        restoreDefaultButton.setFont(Design.getDefaultFont(20));
        restoreDefaultButton.addMouseListener(this);
        restoreDefaultButton.setBackground(Design.getPrimaryColor());
        buttonPanel.add(restoreDefaultButton);


        abortButton = new JButton("Abbrechen");
        abortButton.setFont(Design.getDefaultFont(20));
        abortButton.addMouseListener(this);
        abortButton.setBackground(Design.getPrimaryColor());
        buttonPanel.add(abortButton);

        leftPlayerPanel.add(player1Label, BorderLayout.NORTH);
        leftPlayerPanel.add(leftPlayerKeySet, BorderLayout.CENTER);
        rightPlayerPanel.add(player2Label, BorderLayout.NORTH);
        rightPlayerPanel.add(rightPlayerKeySet, BorderLayout.CENTER);

        wholePanel.add(leftPlayerPanel, BorderLayout.LINE_START);
        wholePanel.add(rightPlayerPanel, BorderLayout.LINE_END);
        wholePanel.add(buttonPanel, BorderLayout.SOUTH);

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
            if (object instanceof KeyBoardLayout) {
                keyBoardLayout = (KeyBoardLayout) object;
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
            boolean isButtonAvailable = true;
            char key = e.getKeyChar();

            if (!(((int) key >= 65 && (int) key <= 90) || ((int) key >= 97 && (int) key <= 122) || key == 'ö' || key == 'ä' || key == 'ü')) {
                isButtonAvailable = false;
            }
            if (!(((int) key >= 65 && (int) key <= 90) || ((int) key >= 97 && (int) key <= 122) || key == 'ö' || key == 'ä' || key == 'ü')) {
                isButtonAvailable = false;
            }
            if (isButtonAvailable) {
                for (int i = 0; i < player1KeyLabels.length; i++) {
                    if (player1KeyLabels[i].equals(selectedKey)) {
                        player1Keys[i] = Character.toLowerCase(e.getKeyChar());
                    }
                    if (player2KeyLabels[i].equals(selectedKey)) {
                        player2Keys[i] = Character.toLowerCase(e.getKeyChar());
                    }
                }
                selectedKey.setText(Character.toString(Character.toUpperCase(e.getKeyChar())));
            }else{
                JOptionPane.showMessageDialog(this,"Es sind nur Buchstaben erlaubt!","Fehler",JOptionPane.ERROR_MESSAGE);
            }
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
            for (int i = 0; i < player1KeyLabels.length; i++) {
                player1KeyLabels[i].setBorder(raisedBorder);
                player2KeyLabels[i].setBorder(raisedBorder);
            }
            Object source = e.getSource();
            System.out.println(player1KeyLabels[0].toString());
            for (int i = 0; i < player1KeyLabels.length; i++) {
                if (source.equals(player1KeyLabels[i])) {
                    selectedKey = player1KeyLabels[i];
                }
                if (source.equals(player2KeyLabels[i])) {
                    selectedKey = player2KeyLabels[i];
                }
            }
            selectedKey.setBorder(loweredBorder);
        } else if (e.getSource() instanceof JButton) {
            if (e.getSource().equals(saveButton)) {
                boolean saveable = true;
                System.out.println((int) 'ö');
                for (int i = 0; i < player1Keys.length; i++) {
                    if (!(((int) player1Keys[i] >= 65 && (int) player1Keys[i] <= 90) || ((int) player1Keys[i] >= 97 && (int) player1Keys[i] <= 122) || player1Keys[i] == 'ö' || player1Keys[i] == 'ä' || player1Keys[i] == 'ü')) {
                        saveable = false;
                        System.out.println(player1Keys[i]);
                    }
                    if (!(((int) player2Keys[i] >= 65 && (int) player2Keys[i] <= 90) || ((int) player2Keys[i] >= 97 && (int) player2Keys[i] <= 122) || player2Keys[i] == 'ö' || player2Keys[i] == 'ä' || player2Keys[i] == 'ü')) {
                        saveable = false;
                        System.out.println(player2Keys[i]);
                    }
                }
                char[] allKeys = new char[player1Keys.length + player2Keys.length];
                for (int i = 0; i < player1Keys.length; i++) {
                    allKeys[i] = player1Keys[i];
                }
                for (int i = 0; i < player2Keys.length; i++) {
                    allKeys[i + player1Keys.length - 1] = player2Keys[i];
                }
                for (int i = 0; i < allKeys.length - 1; i++) {
                    for (int j = 1 + i; j < allKeys.length - 1; j++) {
                        if (allKeys[i] == allKeys[j]) {
                            saveable = false;
                        }
                    }
                }


                if (saveable) {
                    keyBoardLayout.setKeysForPlayer1(player1Keys);
                    keyBoardLayout.setKeysForPlayer2(player2Keys);
                    keyBoardLayout.serialize();
                    Frame.getInstance().getContentPane().removeAll();
                    Frame.getInstance().getContentPane().add(originalContentPane);
                    if (originalContentPane instanceof GamePanel) {
                        ((GamePanel) originalContentPane).updateKeys();
                        ((GamePanel) originalContentPane).setPanelActive();
                    }
                    ((JPanel) Frame.getInstance().getContentPane()).updateUI();
                }
            } else if (e.getSource().equals(restoreDefaultButton)) {
                player1Keys = KeyBoardLayout.getPlayer1DefaultKeys();
                player2Keys = KeyBoardLayout.getPlayer2DefaultKeys();
                if (selectedKey != null) {
                    selectedKey.setBorder(raisedBorder);
                    selectedKey = null;
                }
                for (int i = 0; i < player1KeyLabels.length; i++) {
                    player1KeyLabels[i].setText(Character.toString(Character.toUpperCase(player1Keys[i])));
                    player2KeyLabels[i].setText(Character.toString(Character.toUpperCase(player2Keys[i])));
                }
            } else if (e.getSource().equals(abortButton)) {
                Frame.getInstance().getContentPane().removeAll();
                Frame.getInstance().getContentPane().add(originalContentPane);
                ((JPanel) Frame.getInstance().getContentPane()).updateUI();
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
