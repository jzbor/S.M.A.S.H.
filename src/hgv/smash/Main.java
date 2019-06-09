package hgv.smash;


import hgv.smash.gui.Frame;
import hgv.smash.gui.MenuPanel;

import javax.swing.*;

public class Main {
    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        JPanel panel = new MenuPanel();
        panel.setSize(Frame.getInstance().getContentPane().getSize());
        Frame.getInstance().getContentPane().add(panel);
        ((JPanel) Frame.getInstance().getContentPane()).updateUI();
       // JPanel panel = new MenuPanel();
      //  panel.setSize(Frame.getInstance().getContentPane().getSize());
    }
}
