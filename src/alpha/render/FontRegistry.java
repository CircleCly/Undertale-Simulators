package alpha.render;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class FontRegistry {
    public static Font getFont(String name, int style, int size) {
        // Try requested font, fallback to SansSerif if not available
        String[] available = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String f : available) {
            if (f.equalsIgnoreCase(name) || f.equals(name)) {
                return new Font(f, style, size);
            }
        }
        // Fallbacks for known custom fonts
        if (name.equals("Monster Friend Back") || name.equals("Determination Sans") || name.equals("宋体")) {
            return new Font("SansSerif", style, size);
        }
        return new Font("SansSerif", style, size);
    }
}
