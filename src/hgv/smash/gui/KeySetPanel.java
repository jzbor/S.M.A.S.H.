package hgv.smash.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeySetPanel extends Panel implements MouseListener {
    private char[] player1Keys =new char[] {'w', 'f', 'a', 'd','r'};
    private char[] player2Keys =new char[] {'i', 'ö', 'j', 'l','p'};

    private JLabel[] player1Label;
    private JLabel[] player2Label;

    private ImageIcon keyNormal;
    private ImageIcon keyPressed;

    private static final int[][] KEY_POSITION = {{1, 0}, {3, 1}, {0, 1}, {2, 1},{3,0}};

    private JLabel selectedKey;

    public KeySetPanel() {
        keyNormal = new ImageIcon("./resources/keyboard/test1.png");
        keyPressed = new ImageIcon("./resources/keyboard/test2.jpg");

        JPanel keySetPanel = new JPanel();
        JPanel leftPlayerKeySet = new JPanel();
        JPanel rightPlayerKeySet = new JPanel();

        keySetPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 280));
        leftPlayerKeySet.setLayout(new GridBagLayout());
        rightPlayerKeySet.setLayout(new GridBagLayout());

        player1Label = new JLabel[5];
        player2Label = new JLabel[5];

        for (int i = 0; i < player1Keys.length; i++) {
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = KEY_POSITION[i][0];
            gbc1.gridy = KEY_POSITION[i][1];
            gbc1.ipadx = 50;
            gbc1.ipady = 50;
            gbc1.insets = new Insets(5, 5, 5, 5);
            player1Label[i] = new JLabel(Character.toString(Character.toUpperCase(player1Keys[i])));
            player1Label[i].setIcon(keyNormal);
            player1Label[i].setIconTextGap(-keyNormal.getIconWidth() / 2);
            player1Label[i].setHorizontalAlignment(JLabel.CENTER);
            player1Label[i].addMouseListener(this);
            player1Label[i].setSize(new Dimension(50, 50));
            player1Label[i].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2, false));
            leftPlayerKeySet.add(player1Label[i], gbc1);


            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = KEY_POSITION[i][0];
            gbc2.gridy = KEY_POSITION[i][1];
            gbc2.ipadx = 70;
            gbc2.ipady = 70;
            gbc2.insets = new Insets(5, 5, 5, 5);
            player2Label[i] = new JLabel(Character.toString(Character.toUpperCase(player2Keys[i])));
            player2Label[i].setIcon(keyNormal);
            player2Label[i].setIconTextGap(-keyNormal.getIconWidth() / 2);
            player2Label[i].setHorizontalAlignment(JLabel.CENTER);
            player2Label[i].addMouseListener(this);
            player2Label[i].setPreferredSize(new Dimension(30, 30));
            player2Label[i].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2, false));
            rightPlayerKeySet.add(player2Label[i], gbc2);
        }

        keySetPanel.add(leftPlayerKeySet);
        keySetPanel.add(rightPlayerKeySet);

        add(keySetPanel);

        OurKeyListener.getInstance().setPanel(this);
        updateUI();


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
        for (int i = 0; i < player1Label.length; i++) {
            player1Label[i].setIcon(keyNormal);
            player1Label[i].setIconTextGap(-keyNormal.getIconWidth() / 2);
            player2Label[i].setIcon(keyNormal);
            player2Label[i].setIconTextGap(-keyNormal.getIconWidth() / 2);
        }
        Object source = e.getSource();
        System.out.println(player1Label[0].toString());
        for (int i = 0; i < player1Label.length; i++) {
            if (source.equals(player1Label[i])) {
                selectedKey = player1Label[i];
                selectedKey.setIcon(keyPressed);
                player1Label[i].setIconTextGap(-keyPressed.getIconWidth() / 2);
            }
            if (source.equals(player2Label[i])) {
                selectedKey = player2Label[i];
                selectedKey.setIcon(keyPressed);
                player2Label[i].setIconTextGap(-keyPressed.getIconWidth() / 2);

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
