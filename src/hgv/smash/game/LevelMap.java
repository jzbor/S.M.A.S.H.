package hgv.smash.game;

import hgv.smash.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelMap extends GameObject {


    public static final String[] MAP_NAMES = {"clouds", "space", "Schmierzettel"};
    private static final String MAP_PATH = "./resources/maps/";

    private BufferedImage backgroundImage;
    private Rectangle[] platformModels;
    private boolean jumpThrough;

    private LevelMap(BufferedImage backgroundImage, Rectangle[] platformModels, boolean jumpThrough) {
        this.backgroundImage = backgroundImage;
        this.platformModels = platformModels;
        this.jumpThrough = jumpThrough;
    }

    public static LevelMap debugMap() throws IOException {
        return load(MAP_PATH + MAP_NAMES[0]);
    }

    public static LevelMap load(String name) throws IOException {
        File bgFile = new File(MAP_PATH + name + "-bg.jpeg");
        BufferedImage bufferedImage = ImageIO.read(bgFile);
        File mapFile = new File(MAP_PATH + name + ".map");

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mapFile)));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
            System.out.println(line);
        }

        boolean jumpup = Boolean.parseBoolean(lines.get(0));

        Rectangle[] rects = new Rectangle[lines.size() - 1];
        for (int i = 1; i < lines.size(); i++) {
            String[] svals = lines.get(i).split(" ");
            int[] ivals = new int[svals.length];
            for (int j = 0; j < svals.length; j++) {
                ivals[j] = Integer.parseInt(svals[j]);
            }
            System.out.println(i + ": " + rects.length + " | " + ivals.length);
            rects[i - 1] = new Rectangle(ivals[0], ivals[1], ivals[2], ivals[3]);
        }

        return new LevelMap(bufferedImage, rects, jumpup);
    }

    public boolean permeable() {
        return jumpThrough;
    }

    public Rectangle[] getPlatformModels() {
        return platformModels;
    }

    public int[] getSpawnPositions() {
        if (platformModels.length == 0)
            return new int[]{0, 0};
        Rectangle rect = platformModels[0];
        return new int[]{
                (int) rect.getX(),
                (int) (rect.getX() + rect.getWidth())
        };
    }

    public boolean intersects(Shape model) {
        for (Rectangle platform :
                platformModels) {
            if (model.intersects(platform))
                return true;
        }
        return false;
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
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
