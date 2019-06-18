package hgv.smash.resources;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Design {

    private static final String PRIMARY_COLOR = "#000000";
    private static final String SECONDARY_COLOR = "#EEEEEE";
    private static final String FONT_DIR = "./resources/fonts/";
    private static final String TITLE_FONT = "ZCOOL_KuaiLe/ZCOOLKuaiLe-Regular.ttf";
    private static final String DEFAULT_FONT = "Russo_One/RussoOne-Regular.ttf";
    private static final String GAMEOVER_FONT = "ZCOOL_KuaiLe/ZCOOLKuaiLe-Regular.ttf";

    public static Color getPrimaryColor() {
        return Color.decode(PRIMARY_COLOR);
    }

    public static Color getSecondaryColor() {
        return Color.decode(SECONDARY_COLOR);
    }

    private static Font getFont(String path, int type, float size) {
        try {
            File file = new File(path);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(type, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

    public static Font getTitleFont() {
        return getFont(FONT_DIR + TITLE_FONT, Font.BOLD, 24);
    }

    public static Font getTitleFont(float size) {
        return getFont(FONT_DIR + TITLE_FONT, Font.BOLD, size);
    }

    public static Font getTitleFont(int type, float size) {
        return getFont(FONT_DIR + TITLE_FONT, type, size);
    }

    public static Font getDefaultFont() {
        return getFont(FONT_DIR + DEFAULT_FONT, Font.PLAIN, 18);
    }

    public static Font getDefaultFont(float size) {
        return getFont(FONT_DIR + DEFAULT_FONT, Font.PLAIN, size);
    }

    public static Font getDefaultFont(int type, float size) {
        return getFont(FONT_DIR + DEFAULT_FONT, type, size);
    }

    public static Font getGameoverFont() {
        return getFont(FONT_DIR + GAMEOVER_FONT, Font.PLAIN, 18);
    }

    public static Font getGameoverFont(float size) {
        return getFont(FONT_DIR + GAMEOVER_FONT, Font.PLAIN, size);
    }

    public static Font getGameoverFont(int type, float size) {
        return getFont(FONT_DIR + GAMEOVER_FONT, type, size);
    }

}
