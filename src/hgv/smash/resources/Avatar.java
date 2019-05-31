package hgv.smash.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Avatar {
    public static final String[] AVATAR_NAMES = {"Avatar1.avatar", "Avatar2"};
    public static final String [] AVATAR_FILES ={"AvatarTest.jpeg"};
    public static final String AVATAR_PATH = "./resources/avatars/";
    public static Avatar debugAvatar() {
        return load(null);
    }

    public static Avatar load(String name) {
        return new Avatar();
    }

}
