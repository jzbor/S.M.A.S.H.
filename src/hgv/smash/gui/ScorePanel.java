package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.LevelMap;
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
    private JButton playButton;
    private JButton storyButton;
    private Player winner;
    private Player looser;
    private LevelMap levelMap;

    public ScorePanel(Player winner, Player looser, LevelMap levelMap) {

        this.winner = winner;
        this.looser = looser;
        this.levelMap = levelMap;

        if (Frame.getInstance().getMusic()) {
            Music oldMusic = Music.getInstanceGameMusic();
            oldMusic.stop();
            Music newMusic=null;
            if (winner.getAvatar().getIndex()==0) {
                newMusic = Music.getInstanceScoreMusicBavaria();

            }
            else if (winner.getAvatar().getIndex()==2) {
                newMusic = Music.getInstanceScoreMusicScout();

            }
            else if(winner.getAvatar().getIndex()==1){
                newMusic=Music.getInstanceScoreMusicSowjet();

            }
            try {
                newMusic.play();
            }
            catch (Exception e){
                System.out.println("kein lied");
            }
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
        JLabel wScoreLabel = new JLabel("Sieger");
        JLabel lIconLabel = new JLabel(new ImageIcon(scaleAvatar(looser.getAvatar())));
        JLabel lNameLabel = new JLabel(looser.getName());
        JLabel lScoreLabel = new JLabel("Opfer");
        storyButton = new JButton("Hintergrund-Geschichte");
        nextButton = new JButton("Hauptmenü");
        playButton = new JButton("Nochmal spielen");

        JPanel scorePanel = new JPanel();
        JPanel p1Panel = new JPanel();
        JPanel nameStoryPanel = new JPanel();
        JPanel p2Panel = new JPanel();
        JPanel buttonPanel = new JPanel();


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
        storyButton.setFont(Design.getDefaultFont(18));
        storyButton.setBorder(paddingBorder);
        storyButton.addActionListener(this);
        //storyButton.setPreferredSize(new Dimension(storyButton.getPreferredSize().width, 22));
        nextButton.setFont(Design.getDefaultFont(24));
        nextButton.setBorder(paddingBorder);
        nextButton.addActionListener(this);
        playButton.setFont(Design.getDefaultFont(24));
        playButton.setBorder(paddingBorder);
        playButton.addActionListener(this);


        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
        p1Panel.setLayout(new BorderLayout());
        nameStoryPanel.setLayout(new BorderLayout());
        p2Panel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new GridLayout());


        setBackground(Design.getPrimaryColor());
        gameoverLabel.setBackground(Design.getPrimaryColor());
        p1Panel.setBackground(Design.getPrimaryColor());
        nameStoryPanel.setBackground(Design.getPrimaryColor());
        p2Panel.setBackground(Design.getPrimaryColor());
        buttonPanel.setBackground(Design.getSecondaryColor());
        gameoverLabel.setForeground(Design.getSecondaryColor());
        wNameLabel.setForeground(Design.getSecondaryColor());
        wScoreLabel.setForeground(Design.getSecondaryColor());
        lNameLabel.setForeground(Design.getSecondaryColor());
        lScoreLabel.setForeground(Design.getSecondaryColor());
        storyButton.setBackground(Design.getPrimaryColor());
        storyButton.setForeground(Design.getSecondaryColor());
        nextButton.setBackground(Design.getPrimaryColor());
        nextButton.setForeground(Design.getSecondaryColor());
        playButton.setBackground(Design.getPrimaryColor());
        playButton.setForeground(Design.getSecondaryColor());

        // Compose layout
        setLayout(new BorderLayout());

        p1Panel.add(wIconLabel, BorderLayout.LINE_START);
        nameStoryPanel.add(wNameLabel, BorderLayout.CENTER);
        nameStoryPanel.add(storyButton, BorderLayout.SOUTH);
        nameStoryPanel.add(wScoreLabel, BorderLayout.LINE_END);
        p1Panel.add(nameStoryPanel, BorderLayout.CENTER);

        p2Panel.add(lIconLabel, BorderLayout.LINE_START);
        p2Panel.add(lNameLabel, BorderLayout.CENTER);
        p2Panel.add(lScoreLabel, BorderLayout.LINE_END);

        scorePanel.add(p1Panel);
        scorePanel.add(p2Panel);

        buttonPanel.add(playButton);
        buttonPanel.add(nextButton);


        add(gameoverLabel, BorderLayout.NORTH);
        add(scorePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
        if (src == storyButton) {
            new TextDialog(winner.getAvatar().getStory(), (Frame) SwingUtilities.getWindowAncestor(this));
        }
        if (src == playButton) {
            GamePanel gamePanel;
            if (winner.getNumber() < looser.getNumber()) {
                gamePanel = new GamePanel(winner.getAvatar(), looser.getAvatar(), levelMap);
            } else {
                gamePanel = new GamePanel(looser.getAvatar(), winner.getAvatar(), levelMap);
            }

            Frame.getInstance().currentpanel(1);
            Frame frame = Frame.getInstance();
            System.out.println(frame);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(gamePanel);
            ((JPanel) (frame.getContentPane())).updateUI();
            Music oldmusic=Music.getInstanceScoreMusicSowjet();
            oldmusic.stop();
            oldmusic=Music.getInstanceScoreMusicBavaria();
            oldmusic.stop();
            oldmusic=Music.getInstanceScoreMusicScout();
            oldmusic.stop();
            Music newmusic=Music.getInstanceGameMusic();
            newmusic.play();
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
