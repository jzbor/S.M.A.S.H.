package hgv.smash.game;

import hgv.smash.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LevelMap extends GameObject {

    public static final String[] MAP_NAMES = {"clouds","stars"};
    public static final String MAP_PATH = "./resources/maps/";
    private BufferedImage backgroundImage;
    private Rectangle[] platformModels;

    public LevelMap(BufferedImage backgroundImage, Rectangle[] platformModels) {
        this.backgroundImage = backgroundImage;
        this.platformModels = platformModels;
    }

    public static LevelMap debugMap() throws IOException {
        return load(MAP_PATH + MAP_NAMES[0]);
    }

    public static LevelMap load(String name) throws IOException {
        File bgFile = new File(name + "-bg.jpeg");
        BufferedImage bufferedImage = ImageIO.read(bgFile);
        Rectangle debugRect = new Rectangle(165, 545, 730, 20);
        return new LevelMap(bufferedImage, new Rectangle[]{debugRect});
    }

    public Rectangle[] getPlatformModels() {
        return platformModels;
    }

    public boolean intersects(Shape model) {
        for (Rectangle platform :
                platformModels) {
            if (model.intersects(platform))
                return true;
        }
        return false;
    }

    @Override
    public void collide(GameObject go) {

    }

    @Override
    public void calc(long millis) {

    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(backgroundImage, 0, 0, null);

        if (Main.DEBUG) {
            // @DEBUG
            graphics2D.setColor(Color.MAGENTA);
            for (Rectangle rect :
                    platformModels) {
                graphics2D.fill(rect);
            }
        }
    }

}
