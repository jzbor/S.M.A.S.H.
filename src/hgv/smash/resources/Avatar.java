package hgv.smash.resources;

public class Avatar {
    public static final String[] AVATAR_NAMES = {"Avatar1", "Avatar2"};
    private static final String AVATAR_PATH = "./resources/avatars/";

    public static Avatar debugAvatar() {
        return load(null);
    }

    public static Avatar load(String name) {
        return new Avatar();
    }

}
