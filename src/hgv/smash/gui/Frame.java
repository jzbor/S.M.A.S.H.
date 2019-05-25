package hgv.smash.gui;

import hgv.smash.game.LevelMap;
import hgv.smash.resources.Avatar;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Frame extends JFrame {

    private static Frame ourInstance = new Frame();
    private static final String TITLE = "S.M.A.S.H.";

    private Frame() {
        super(TITLE);

        // Init Frame
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(1024, 768));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createLineBorder(Color.RED)); // debugging purposes

        try {
            Avatar testAvatar1 = Avatar.debugAvatar();
            Avatar testAvatar2 = Avatar.debugAvatar();
            LevelMap levelMap = LevelMap.debugMap();
            setContentPane(new GamePanel(testAvatar1, testAvatar2, levelMap));
        } catch (IOException e) {
            e.printStackTrace();
            setContentPane(new MenuPanel());
        }
    }

    public static Frame getInstance() {
        return ourInstance;
    }
}
