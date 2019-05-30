package hgv.smash.resources;

public class Avatar {
    public static final String[] AVATAR_NAMES = {"Georg", "Avatar2"};
    public static final String [] AVATAR_FILES ={"AvatarTest.jpeg"};
    public static final String AVATAR_PATH = "./resources/avatars/";
    public static Avatar debugAvatar() {
        return load(null);
    }

    public static Avatar load(String name) {
        return new Avatar();
    }

}
