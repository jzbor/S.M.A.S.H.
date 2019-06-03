package hgv.smash.resources;

import hgv.smash.exceptions.AvatarNotAvailableException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Avatar {
    public static final String[] AVATAR_NAMES = {"Georg", "Avatar2"};
    public static final String[] AVATAR_FILES = {"georg/"};
    public static final int NORMAL = 0;
    public static final int STANDING = 1;
    public static final int WALKING_1 = 2;
    public static final int WALKING_2 = 3;
    public static final int WALKING_L = 4;
    public static final int WALKING_R = 5;
    public static final int JUMPING = 6;
    public static final String AVATAR_PATH = "./resources/avatars/";
    public static final String[] FILENAMES = {"BNormal.png", "BStand.png", "BWalk1.png", "BWalk2.png"}; // @TODO make private
    private BufferedImage[] images;

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
        images = new BufferedImage[FILENAMES.length];
        for (int i = 0; i < FILENAMES.length; i++) {
            File file = new File(AVATAR_PATH + AVATAR_FILES[index] + FILENAMES[i]);
            try {
                images[i] = ImageIO.read(file);
            } catch (IOException e) {
                throw new AvatarNotAvailableException("File " + file.getName() + " not available", e);
            }
        }
    }

    public BufferedImage getImage(int animation) {
        return images[animation];
    }

    public void draw(Graphics2D graphics2D, int x, int y, int state) {
        graphics2D.drawImage(images[0], x, y, null);
    }

}
