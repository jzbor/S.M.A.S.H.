package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;
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
        JLabel wIconLabel = new JLabel(new ImageIcon(winner.getAvatar().getImage(Avatar.NORMAL)));
        JLabel lIconLabel = new JLabel(new ImageIcon(winner.getAvatar().getImage(Avatar.NORMAL)));

        // Configure elements
        gameoverLabel.setFont(Design.getGameoverFont(80));
        gameoverLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Compose layout
        setLayout(new BorderLayout());
        add(gameoverLabel, BorderLayout.NORTH);
        setSize(Frame.getInstance().getContentPane().getSize());
        setPreferredSize(getSize());


        // debugging
        if (Main.DEBUG) {
            setBorder(BorderFactory.createLineBorder(Color.RED)); // debugging purposes
            updateUI();
        }
        System.out.println(gameoverLabel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
