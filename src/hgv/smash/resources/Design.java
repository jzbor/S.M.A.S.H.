package hgv.smash.resources;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Design {

    private static final String PRIMARY_COLOR = "#000000";
    private static final String SECONDARY_COLOR = "#000000";
    private static final String FONT_DIR = "./resources/fonts/";
    private static final String TITLE_FONT = "ZCOOL_KuaiLe/ZCOOLKuaiLe-Regular.ttf";
    private static final String DEFAULT_FONT = "ZCOOL_KuaiLe/ZCOOLKuaiLe-Regular.ttf";

    public static Color getPrimaryColor() {
        return Color.decode(PRIMARY_COLOR);
    }

    public static Color getSecondaryColor() {
        return Color.decode(SECONDARY_COLOR);
    }

    public static Font getTitleFont() {
        try {
            File file = new File(FONT_DIR + TITLE_FONT);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(Font.BOLD, 24);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

    public static Font getTitleFont(float size) {
        try {
            File file = new File(FONT_DIR + TITLE_FONT);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(Font.BOLD, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

    public static Font getTitleFont(int type, float size) {
        try {
            File file = new File(FONT_DIR + TITLE_FONT);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(type, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

    public static Font getDefaultFont() {
        try {
            File file = new File(FONT_DIR + DEFAULT_FONT);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(Font.BOLD, 24);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

    public static Font getDefaultFont(float size) {
        try {
            File file = new File(FONT_DIR + DEFAULT_FONT);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(Font.BOLD, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

    public static Font getDefaultFont(int type, float size) {
        try {
            File file = new File(FONT_DIR + DEFAULT_FONT);
            return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(type, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return Font.getFont(Font.SANS_SERIF);
        }
    }

}
