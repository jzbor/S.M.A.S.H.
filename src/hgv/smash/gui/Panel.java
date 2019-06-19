package hgv.smash.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public abstract class Panel extends JPanel {
    public Panel(){
        super();
    }
    public abstract void keyTyped(KeyEvent keyEvent);
    public abstract void keyPressed(KeyEvent keyEvent);
    public abstract void keyReleased(KeyEvent keyEvent);

    public static BufferedImage scaleImage(BufferedImage img, int width, int height) {
        BufferedImage newImg = new BufferedImage(width, height, img.getType());
        Graphics2D graphics2D = newImg.createGraphics();
        graphics2D.drawImage(img, 0, 0, width, height, null);
        graphics2D.dispose();
        return newImg;
    }

    public static BufferedImage scaleImgToHeight(BufferedImage img, int height) {
        return scaleImage(img, img.getWidth() / img.getHeight() * height, height);
    }

    public static BufferedImage scaleImgToWidth(BufferedImage img, int width) {
        return scaleImage(img, width, img.getHeight() / img.getWidth() * width);
    }

}
