package hgv.smash.gui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private static Frame ourInstance = new Frame();
    private static final String TITLE = "S.M.A.S.H.";
    private static final Dimension FINAL_SIZE = new Dimension(1024, 768);
    private static final Dimension DEBUG_SIZE = new Dimension(1366, 768);

    private Frame() {
        super(TITLE);

        // Init Frame
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(1024, 768));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createLineBorder(Color.RED)); // debugging purposes

        setContentPane(new MenuPanel());
    }

    public static Frame getInstance() {
        return ourInstance;
    }
}
