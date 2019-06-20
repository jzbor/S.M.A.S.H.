package hgv.smash.resources;

import hgv.smash.exceptions.AvatarNotAvailableException;
import hgv.smash.game.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Avatar {
    public static final String[] AVATAR_NAMES = {"Georg", "Genosse Geist"};
    public static final int NORMAL = 0;
    public static final int STANDING = 1;
    public static final int SUPER_LOADING = 0;
    public static final int SUPER_READY = 1;
    private static final int HIT = 2;
    private static final int SUPER = 3;
    private static final String AVATAR_PATH = "./resources/avatars/";
    private static final String[] REGULAR_FILENAMES = {"normal.png", "stand.png", "hit.png", "super.png"};
    private static final String[] ICON_FILENAMES = {"superloading.png", "superready.png"};
    private static final String ANIMATION_FILENAMES = "walk%d.png";
    private static final String STORY_FILENAME = "story.txt";
    private static final String[] AVATAR_FILES = {"georg/", "ghost/"};
    private static final int HIT_ANIMATION_LENGTH = 400;
    private BufferedImage[] regularImages;
    private BufferedImage[] animationImages;
    private BufferedImage[] icons;
    private int[] lastAnimation = new int[2];
    private long lastHit;
    private long lastSuperHit;
    private String story;

    private Avatar(String name) throws AvatarNotAvailableException {
        int index = -1;
        for (int i = 0; i < AVATAR_NAMES.length; i++) {
            if (AVATAR_NAMES[i].equals(name)) {
                index = i;
                break;
            }
        }

        init(index);

    }

    public Avatar(int index) throws AvatarNotAvailableException {
        init(index);
    }

    public static Avatar debugAvatar() {
        try {
            return new Avatar("Georg");
        } catch (AvatarNotAvailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void init(int index) throws AvatarNotAvailableException {
        if (index < 0 || index >= AVATAR_FILES.length) {
            throw new AvatarNotAvailableException("Index " + index + "not available");
        }

        story = "";

        File currentFile = new File(AVATAR_PATH + AVATAR_FILES[index] + STORY_FILENAME);
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(currentFile));
            int i;
            while ((i = isr.read()) != -1) {
                story += (char) i;
            }
        } catch (IOException e) {
            throw new AvatarNotAvailableException("File " + currentFile.getName() + " not available", e);
        }

        regularImages = new BufferedImage[REGULAR_FILENAMES.length];
        for (int i = 0; i < REGULAR_FILENAMES.length; i++) {
            currentFile = new File(AVATAR_PATH + AVATAR_FILES[index] + REGULAR_FILENAMES[i]);
            try {
                regularImages[i] = ImageIO.read(currentFile);
            } catch (IOException e) {
                throw new AvatarNotAvailableException("File " + currentFile.getName() + " not available", e);
            }
        }

        List<BufferedImage> list = new ArrayList<>();
        currentFile = new File(AVATAR_PATH + AVATAR_FILES[index] + String.format(ANIMATION_FILENAMES, 0));
        System.out.println(currentFile.getPath());
        for (int i = 0; currentFile.exists(); i++) {
            try {
                list.add(ImageIO.read(currentFile));
            } catch (IOException e) {
                throw new AvatarNotAvailableException("File " + currentFile.getName() + " not available", e);
            }
            currentFile = new File(AVATAR_PATH + AVATAR_FILES[index] + String.format(ANIMATION_FILENAMES, i + 1));
        }
        animationImages = list.toArray(new BufferedImage[0]);
        if (animationImages.length == 0) {
            throw new AvatarNotAvailableException("No animation files available");
        }

        icons = new BufferedImage[ICON_FILENAMES.length];
        for (int i = 0; i < ICON_FILENAMES.length; i++) {
            currentFile = new File(AVATAR_PATH + AVATAR_FILES[index] + ICON_FILENAMES[i]);
            try {
                icons[i] = ImageIO.read(currentFile);
            } catch (IOException e) {
                throw new AvatarNotAvailableException("File " + currentFile.getName() + " not available", e);
            }
        }
    }

    public BufferedImage getImage(int animation) {
        return regularImages[animation];
    }

    public BufferedImage getIcon(int icon) {
        return icons[icon];
    }

    public String getStory() {
        return story;
    }

    public void draw(Graphics2D graphics2D, int x, int y, int state) {
        boolean hit = (System.currentTimeMillis() - lastHit < HIT_ANIMATION_LENGTH);
        boolean superhit = (System.currentTimeMillis() - lastSuperHit < HIT_ANIMATION_LENGTH);

        if (state == Player.Movement.STOP_MOVING) {
            if (hit)
                graphics2D.drawImage(regularImages[HIT], x, y, null);
            else if (superhit)
                graphics2D.drawImage(regularImages[SUPER], x, y, null);
            else
                graphics2D.drawImage(regularImages[NORMAL], x, y, null);
        } else if (state == Player.Movement.MOVE_LEFT) {
            BufferedImage image;

            int val = (int) ((System.currentTimeMillis() / 100) % animationImages.length);
            image = animationImages[val];

            if (hit)
                image = regularImages[HIT];
            else if (superhit)
                image = regularImages[SUPER];

            graphics2D.drawImage(image, x + (getImage(NORMAL).getWidth() - image.getWidth()), y, null);
        } else if (state == Player.Movement.MOVE_RIGHT) {
            BufferedImage image;

            int val = (int) ((System.currentTimeMillis() / 100) % animationImages.length);
            image = animationImages[val];

            if (hit)
                image = regularImages[HIT];
            else if (superhit)
                image = regularImages[SUPER];

            // Flip image
            image = flipImage(image);
            graphics2D.drawImage(image, x, y, null);
        } else {
            if (hit)
                graphics2D.drawImage(regularImages[HIT], x, y, null);
            else if (superhit)
                graphics2D.drawImage(regularImages[SUPER], x, y, null);
            else
                graphics2D.drawImage(regularImages[NORMAL], x, y, null);
        }
    }

    private BufferedImage flipImage(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                image.getType());
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }


    public void setLastHit(long lastHit) {
        this.lastHit = lastHit;
    }

    public void setLastSuperHit(long lastSuperHit) {
        this.lastSuperHit = lastSuperHit;
    }


}
