package hgv.smash;


import hgv.smash.gui.Frame;
import hgv.smash.gui.MenuPanel;
import hgv.smash.resources.Design;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        // remove unnecessary outputs
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        }));
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        }));
        Design.init();
        JPanel panel = new MenuPanel();
        panel.setSize(Frame.getInstance().getContentPane().getSize());
        Frame.getInstance().getContentPane().add(panel);
        ((JPanel) Frame.getInstance().getContentPane()).updateUI();
    }
}
