package com.amplicube.opalhud;

import java.awt.*;

public class utils {
    public static int ColorToInt(Color color, boolean useAlpha) {
        if (useAlpha) {
            return (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
        }
        else {
            return (255 << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
        }
    }
}
