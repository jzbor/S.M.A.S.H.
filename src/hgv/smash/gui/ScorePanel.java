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
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class ScorePanel extends Panel implements ActionListener {

    private int biggestWidth;
    private JButton nextButton;

    public ScorePanel(Player winner, Player looser, BufferedImage lastFrame) {

        if (Frame.getInstance().getMusic()) {
            Music oldMusic = Music.getInstanceGameMusic();
            oldMusic.stop();
            Music newMusic = Music.getInstanceScoreMusic();
            newMusic.play();
        }
        Frame.getInstance().currentpanel(2);

        // define width of icon container; call before loading the icon
        int wwidth = winner.getAvatar().getImage(Avatar.NORMAL).getWidth();
        int lwidth = looser.getAvatar().getImage(Avatar.NORMAL).getWidth();
        if (wwidth > lwidth) {
            biggestWidth = wwidth;
        } else {
            biggestWidth = lwidth;
        }

        // Create elements
        JLabel gameoverLabel = new JLabel("Game Over");
        JLabel wIconLabel = new JLabel(new ImageIcon(scaleAvatar(winner.getAvatar())));
        JLabel wNameLabel = new JLabel(winner.getName());
        JLabel wScoreLabel = new JLabel("Winner");
        JLabel lIconLabel = new JLabel(new ImageIcon(scaleAvatar(looser.getAvatar())));
        JLabel lNameLabel = new JLabel(looser.getName());
        JLabel lScoreLabel = new JLabel("Looser");
        nextButton = new JButton("Next");

        JPanel scorePanel = new JPanel();
        JPanel p1Panel = new JPanel();
        JPanel p2Panel = new JPanel();


        // Configure elements
        Border paddingBorder = new EmptyBorder(30, 30, 30, 30);
        gameoverLabel.setFont(Design.getGameoverFont(80));
        gameoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameoverLabel.setBorder(paddingBorder);

        wIconLabel.setBorder(paddingBorder);
        wNameLabel.setFont(Design.getDefaultFont(24));
        wNameLabel.setBorder(paddingBorder);
        wScoreLabel.setFont(Design.getDefaultFont(24));
        wScoreLabel.setBorder(paddingBorder);
        lIconLabel.setBorder(paddingBorder);
        lNameLabel.setFont(Design.getDefaultFont(24));
        lNameLabel.setBorder(paddingBorder);
        lScoreLabel.setFont(Design.getDefaultFont(24));
        lScoreLabel.setBorder(paddingBorder);
        nextButton.setFont(Design.getDefaultFont(24));
        nextButton.setBorder(paddingBorder);
        nextButton.addActionListener(this);
        nextButton.setBackground(Color.WHITE);

        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
        p1Panel.setLayout(new BorderLayout());
        p2Panel.setLayout(new BorderLayout());


        setBackground(Design.getPrimaryColor());
        gameoverLabel.setBackground(Design.getPrimaryColor());
        p1Panel.setBackground(Design.getPrimaryColor());
        p2Panel.setBackground(Design.getPrimaryColor());
        gameoverLabel.setForeground(Design.getSecondaryColor());
        wNameLabel.setForeground(Design.getSecondaryColor());
        wScoreLabel.setForeground(Design.getSecondaryColor());
        lNameLabel.setForeground(Design.getSecondaryColor());
        lScoreLabel.setForeground(Design.getSecondaryColor());
        nextButton.setBackground(Design.getPrimaryColor());
        nextButton.setForeground(Design.getSecondaryColor());

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
        add(nextButton, BorderLayout.SOUTH);
        setSize(Frame.getInstance().getContentPane().getSize());
        setPreferredSize(getSize());


        // debugging
        if (Main.DEBUG) {
            setBorder(BorderFactory.createLineBorder(Color.RED)); // debugging purposes
            updateUI();
        }
        System.out.println(gameoverLabel);
    }

    private BufferedImage scaleAvatar(Avatar avatar) {
        BufferedImage origImg = avatar.getImage(Avatar.NORMAL);
        BufferedImage newImg = new BufferedImage(biggestWidth, origImg.getHeight(), origImg.getType());
        Graphics2D graphics2D = newImg.createGraphics();
        graphics2D.drawImage(origImg, newImg.getWidth() / 2 - origImg.getWidth() / 2, 0, (int) (origImg.getWidth() / 1.2), (int) (origImg.getHeight() / 1.2), null);
        graphics2D.dispose();
        return newImg;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object src = actionEvent.getSource();

        if (src == nextButton) {
            Panel panel = new MenuPanel();
            Frame.getInstance().getContentPane().removeAll();
            Frame.getInstance().getContentPane().add(panel);
            ((JPanel) Frame.getInstance().getContentPane().add(panel)).updateUI();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
