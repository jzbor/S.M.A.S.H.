package hgv.smash.resources;

import hgv.smash.exceptions.AvatarNotAvailableException;
import hgv.smash.game.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Avatar {
    public static final String[] AVATAR_NAMES = {"Georg", "Genosse Geist"};
    public static final int NORMAL = 0;
    public static final int STANDING = 1;
    private static final int WALKING_1 = 2;
    private static final int WALKING_2 = 3;
    public static final int WALKING_L = 4;
    public static final int WALKING_R = 5;
    public static final int JUMPING = 6;
    private static final String AVATAR_PATH = "./resources/avatars/";
    private static final String[] REGULAR_FILENAMES = {"normal.png", "stand.png", "hit.png"};
    private static final String[] ICON_FILENAMES = {"superloading.png", "superready.png"};
    private static final String ANIMATION_FILENAMES = "walk%d.png";
    private static final String[] AVATAR_FILES = {"georg/", "ghost/"};
    private BufferedImage[] regularImages;
    private BufferedImage[] animationImages;
    private BufferedImage[] icons;
    private int[] lastAnimation = new int[2];

    public Avatar(String name) throws AvatarNotAvailableException {
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
        regularImages = new BufferedImage[REGULAR_FILENAMES.length];
        for (int i = 0; i < REGULAR_FILENAMES.length; i++) {
            File file = new File(AVATAR_PATH + AVATAR_FILES[index] + REGULAR_FILENAMES[i]);
            try {
                regularImages[i] = ImageIO.read(file);
            } catch (IOException e) {
                throw new AvatarNotAvailableException("File " + file.getName() + " not available", e);
            }
        }
        List<BufferedImage> list = new ArrayList<>();
        File f = new File(AVATAR_PATH + AVATAR_FILES[index] + String.format(ANIMATION_FILENAMES, 0));
        System.out.println(f.getPath());
        for (int i = 0; f.exists(); i++) {
            try {
                list.add(ImageIO.read(f));
            } catch (IOException e) {
                throw new AvatarNotAvailableException("File " + f.getName() + " not available", e);
            }
            f = new File(AVATAR_PATH + AVATAR_FILES[index] + String.format(ANIMATION_FILENAMES, i + 1));
        }
        animationImages = list.toArray(new BufferedImage[0]);
        if (animationImages.length == 0) {
            throw new AvatarNotAvailableException("No animation files available");
        }
    }

    public BufferedImage getImage(int animation) {
        return regularImages[animation];
    }

    public void draw(Graphics2D graphics2D, int x, int y, int state) {

        if (state == Player.Movement.STOP_MOVING) {
            graphics2D.drawImage(regularImages[NORMAL], x, y, null);
        } else if (state == Player.Movement.MOVE_LEFT) {
            BufferedImage image;

            int val = (int) ((System.currentTimeMillis() / 100) % animationImages.length);
            image = animationImages[val];

            graphics2D.drawImage(image, x + (getImage(NORMAL).getWidth() - image.getWidth()), y, null);
        } else if (state == Player.Movement.MOVE_RIGHT) {
            BufferedImage image;

            int val = (int) ((System.currentTimeMillis() / 100) % animationImages.length);
            image = animationImages[val];

            // Flip image
            image = flipImage(image);
            graphics2D.drawImage(image, x, y, null);
        } else {
            graphics2D.drawImage(regularImages[0], x, y, null);
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

}
