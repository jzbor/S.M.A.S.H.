package hgv.smash.gui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageExtract {
    private int xOffset;
    private int yOffset;
    private int width;
    private int height;
    private BufferedImage image;
    public ImageExtract(int xOffset,int yOffset,int width,int height,BufferedImage image){
        this.xOffset=xOffset;
        this.yOffset=yOffset;
        this.width=width;
        this.height=height;
        this.image=image;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image){
        this.image=image;
    }
}
