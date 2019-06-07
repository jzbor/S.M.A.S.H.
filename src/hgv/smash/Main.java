package hgv.smash;


import hgv.smash.gui.Frame;
import hgv.smash.gui.MenuPanel;

import javax.swing.*;

public class Main {
    public static final boolean DEBUG = false;

    public static void main(String[] args) {
        JPanel panel = new MenuPanel();
        panel.setSize(Frame.getInstance().getContentPane().getSize());
    }
}
