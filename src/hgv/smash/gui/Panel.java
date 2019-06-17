package hgv.smash.gui;

import javax.swing.*;
import java.awt.event.KeyEvent;

public abstract class Panel extends JPanel {
    public Panel(){
        super();
    }
    public abstract void keyTyped(KeyEvent keyEvent);
    public abstract void keyPressed(KeyEvent keyEvent);
    public abstract void keyReleased(KeyEvent keyEvent);

}
