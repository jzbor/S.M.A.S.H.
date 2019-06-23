package hgv.smash.resources;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Design {

    private static final String PRIMARY_COLOR = "#6d0013";
    private static final String SECONDARY_COLOR = "#B9AFAF";
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
        return getFont(FONT_DIR + DEFAULT_FONT, Font.PLAIN, 15);
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

    public static void init() {
        String[] bgKeys = new String[]{
                "Panel.background",
                "Button.background",
                "ComboBox.background",
                "ComboBox.buttonBackground",
                "ComboBox.selectionForeground",
                "OptionPane.background",
                "MenuBar.selectionForeground",
                "MenuItem.selectionForeground",
                "MenuBar.background",
                "Menu.selectionForeground"
        };
        for (int i = 0; i < bgKeys.length; i++) {
            UIManager.put(bgKeys[i], getPrimaryColor());
        }

        String[] fgKeys = new String[]{
                "ComboBox.selectionBackground",
                "Button.foreground",
                "Button.highlight",
                "Button.select",
                "OptionPane.foreground",
                "OptionPane.messageForeground",
                "OptionPane.buttonForeground",
                "Panel.foreground",
                "MenuBar.selectionBackground",
                "MenuItem.selectionBackground",
                "MenuBar.highlight",
                "MenuBar.foreground",
                "Menu.selectionBackground"
        };
        for (int i = 0; i < fgKeys.length; i++) {
            UIManager.put(fgKeys[i], getSecondaryColor());
        }

        String[] fontKeys = new String[]{
                "OptionPane.font",
                "OptionPane.messageFont",
                "OptionPane.buttonFont"
        };
        for (int i = 0; i < fontKeys.length; i++) {
            UIManager.put(fontKeys[i], getDefaultFont());
        }

        String[] borderKeys = new String[]{
                "MenuBar.border",
                "MenuItem.border",
                "Menu.border"
        };
        for (int i = 0; i < borderKeys.length; i++) {
            UIManager.put(borderKeys[i], BorderFactory.createEmptyBorder());
        }
    }
}
