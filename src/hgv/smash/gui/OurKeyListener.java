package hgv.smash.gui;

import hgv.smash.gui.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OurKeyListener extends Component implements KeyListener, KeyEventDispatcher {
    public static OurKeyListener ourInstance=new OurKeyListener();
    public hgv.smash.gui.Panel panel;

    private OurKeyListener(){
        panel=null;
    }

    public static OurKeyListener getInstance(){
        return ourInstance;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
        removeKeyListener(this);
        addKeyListener(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        processKeyEvent(e);
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        panel.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        panel.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        panel.keyReleased(e);
    }
}