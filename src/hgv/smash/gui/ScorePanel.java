package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Design;
import hgv.smash.resources.Music;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ScorePanel extends Panel implements ActionListener {

    private Player winner;
    private Player looser;
    private BufferedImage lastFrame;

    public ScorePanel(Player winner, Player looser, BufferedImage lastFrame) {
        Music oldMusic= Music.getInstanceGameMusic();
        oldMusic.stop();
        Music newMusic = Music.getOurInstanceScoreMusic();
        newMusic.play();
        this.winner = winner;
        this.looser = looser;
        this.lastFrame = lastFrame;

        // Create elements
        JLabel gameoverLabel = new JLabel("Game Over");
        JLabel wIconLabel = new JLabel(new ImageIcon(winner.getAvatar().getImage(Avatar.NORMAL)));
        JLabel wNameLabel = new JLabel(winner.toString());
        JLabel wScoreLabel = new JLabel("Winner");
        JLabel lIconLabel = new JLabel(new ImageIcon(winner.getAvatar().getImage(Avatar.NORMAL)));
        JLabel lNameLabel = new JLabel(looser.toString());
        JLabel lScoreLabel = new JLabel("Looser");

        JPanel scorePanel = new JPanel();
        JPanel p1Panel = new JPanel();
        JPanel p2Panel = new JPanel();


        // Configure elements
        Border paddingBorder = new EmptyBorder(30, 30, 30, 30);
        gameoverLabel.setFont(Design.getGameoverFont(80));
        gameoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameoverLabel.setBorder(paddingBorder);

        wIconLabel.setBorder(paddingBorder);
        wNameLabel.setFont(Design.getDefaultFont());
        wNameLabel.setBorder(paddingBorder);
        wScoreLabel.setFont(Design.getDefaultFont());
        wScoreLabel.setBorder(paddingBorder);
        lIconLabel.setBorder(paddingBorder);
        lNameLabel.setFont(Design.getDefaultFont());
        lNameLabel.setBorder(paddingBorder);
        lScoreLabel.setFont(Design.getDefaultFont());
        lScoreLabel.setBorder(paddingBorder);

        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
        p1Panel.setLayout(new BorderLayout());
        p2Panel.setLayout(new BorderLayout());

        // Compose layout
        setLayout(new BorderLayout());

        p1Panel.add(wIconLabel, BorderLayout.LINE_START);
        p1Panel.add(wNameLabel, BorderLayout.CENTER);
        p1Panel.add(wScoreLabel, BorderLayout.LINE_END);

        p2Panel.add(lIconLabel, BorderLayout.LINE_START);
        p2Panel.add(lNameLabel, BorderLayout.CENTER);
        p2Panel.add(lScoreLabel, BorderLayout.LINE_END);

        scorePanel.add(p1Panel);
        scorePanel.add(p2Panel);


        add(gameoverLabel, BorderLayout.NORTH);
        add(scorePanel, BorderLayout.CENTER);
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
