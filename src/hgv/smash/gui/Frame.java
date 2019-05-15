package hgv.smash.gui;

import javax.swing.*;

public class Frame extends JFrame {

    private static Frame ourInstance = new Frame();

    private Frame() {
    }

    public static Frame getInstance() {
        return ourInstance;
    }
}
