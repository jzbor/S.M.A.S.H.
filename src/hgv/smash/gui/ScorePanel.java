package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.Player;
import hgv.smash.resources.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ScorePanel extends Panel implements ActionListener {

    private Player winner;
    private Player looser;
    private BufferedImage lastFrame;

    public ScorePanel(Player winner, Player looser, BufferedImage lastFrame) {
        this.winner = winner;
        this.looser = looser;
        this.lastFrame = lastFrame;

        // Create elements
        JLabel gameoverLabel = new JLabel("Game Over");
        Box mainBox = Box.createVerticalBox();

        // Configure elements
        gameoverLabel.setFont(Design.getDefaultFont());

        // Compose layout
        setLayout(new BorderLayout());
        mainBox.add(gameoverLabel);
        add(gameoverLabel, BorderLayout.NORTH);
        setVisible(true);
        setSize(1014, 710);
        setPreferredSize(getSize());


        // debugging
        if (Main.DEBUG) {
            setBorder(BorderFactory.createLineBorder(Color.RED)); // debugging purposes
            gameoverLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
            updateUI();

            System.out.println("This: " + this);
            System.out.println("Frame: " + Frame.getInstance());
            System.out.println("Content: " + Frame.getInstance().getContentPane());
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
