package hgv.smash.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeySetPanel extends Panel implements ActionListener {
    private ButtonGroup buttonGroup;
    private JToggleButton[] player1Button;
    private char[] player1Keys = {'w', 'f', 'a', 's', 'd'};
    private JToggleButton[] player2Button;
    private char[] player2Keys = {'i', 'ö', 'j', 'k', 'l'};

    private int keyPosition[][] = {{1, 0}, {2, 0}, {0, 1}, {1, 1}, {2, 1}};

    private JToggleButton selectedKey;

    public KeySetPanel() {
        buttonGroup=new ButtonGroup();

        JPanel keySetPanel = new JPanel();
        JPanel leftPlayerKeySet = new JPanel();
        JPanel rightPlayerKeySet = new JPanel();

        keySetPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftPlayerKeySet.setLayout(new GridBagLayout());
        rightPlayerKeySet.setLayout(new GridBagLayout());

        player1Button = new JToggleButton[5];
        player2Button = new JToggleButton[5];
        for (int i = 0; i < player1Keys.length; i++) {
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = keyPosition[i][0];
            gbc1.gridy = keyPosition[i][1];
            gbc1.ipadx = 100;
            gbc1.ipady = 100;
            gbc1.insets=new Insets(5,5,5,5);
            player1Button[i] = new JToggleButton(Character.toString(player1Keys[i]));
            buttonGroup.add(player1Button[i]);
            player1Button[i].addActionListener(this);
            player1Button[i].setPreferredSize(new Dimension(50,50));
            leftPlayerKeySet.add(player1Button[i], gbc1);


            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = keyPosition[i][0];
            gbc2.gridy = keyPosition[i][1];
            gbc2.ipadx = 100;
            gbc2.ipady = 100;
            gbc2.insets=new Insets(5,5,5,5);
            player2Button[i] = new JToggleButton(Character.toString(player2Keys[i]));
            buttonGroup.add(player2Button[i]);
            player2Button[i].addActionListener(this);
            player2Button[i].setPreferredSize(new Dimension(50,50));
            rightPlayerKeySet.add(player2Button[i], gbc2);
        }

        keySetPanel.add(leftPlayerKeySet);
        keySetPanel.add(rightPlayerKeySet);

        add(keySetPanel);

        OurKeyListener.getInstance().setPanel(this);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (int i = 0; i < player1Button.length; i++) {
            if (source.equals(player1Button[i])) {
                selectedKey=player1Button[i];
            }
            if (source.equals(player2Button[i])) {
                selectedKey=player2Button[i];
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if(selectedKey!=null){
            for(int i=0;i<player1Button.length;i++){
                if(player1Button[i].equals(selectedKey)){
                    player1Keys[i]=e.getKeyChar();
                }
                if(player2Button[i].equals(selectedKey)){
                    player2Keys[i]=e.getKeyChar();
                }
            }
            selectedKey.setText(Character.toString(e.getKeyChar()));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
