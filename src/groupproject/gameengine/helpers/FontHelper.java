package groupproject.gameengine.helpers;

import java.awt.*;
import java.io.File;

public class FontHelper {

    protected static final String FONT_FOLDER = "assets/fonts";

    private FontHelper() {
    }

    public static Font get(String fontString, float size) {
        try {
            String fontPath = String.format("%s/%s", FONT_FOLDER, fontString);
            File file = new File(fontPath);
            Font font = Font.createFont(Font.TRUETYPE_FONT, file);
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font(Font.MONOSPACED, Font.PLAIN, (int) size);
        }
    }

}
