package hgv.smash;


import hgv.smash.gui.Frame;
import hgv.smash.gui.MenuPanel;
import hgv.smash.resources.Design;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.setOut(null);
        System.setErr(null);
        System.setIn(null);
        Design.init();
        JPanel panel = new MenuPanel();
        panel.setSize(Frame.getInstance().getContentPane().getSize());
        Frame.getInstance().getContentPane().add(panel);
        ((JPanel) Frame.getInstance().getContentPane()).updateUI();
    }
}
