package hgv.smash.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelMap extends GameObject {


    public static final String[] MAP_NAMES = {"Wolken", "Weltraum", "Schmierzettel"};
    private static final String[] MAP_FILES = {"clouds", "space", "scratchpad"};
    private static final String MAP_PATH = "./resources/maps/";

    private BufferedImage backgroundImage;
    private Rectangle[] platformModels;
    private boolean jumpThrough;

    private LevelMap(BufferedImage backgroundImage, Rectangle[] platformModels, boolean jumpThrough) {
        this.backgroundImage = backgroundImage;
        this.platformModels = platformModels;
        this.jumpThrough = jumpThrough;
    }

    public static LevelMap load(String name) throws IOException {
        int index = -1;
        for (int i = 0; i < MAP_NAMES.length; i++) {
            if (MAP_NAMES[i].equals(name)) {
                index = i;
                break;
            }
        }

        String id = MAP_FILES[index];

        File bgFile = new File(MAP_PATH + id + "-bg.jpeg");
        BufferedImage bufferedImage = ImageIO.read(bgFile);
        File mapFile = new File(MAP_PATH + id + ".map");

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

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void calc(long millis) {

    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(backgroundImage, 0, 0, null);
    }

}
