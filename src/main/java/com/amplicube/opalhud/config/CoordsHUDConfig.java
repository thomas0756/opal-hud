package com.amplicube.opalhud.config;

import dev.wooferz.hudlib.hud.HUDConfig;

import java.awt.*;

public class CoordsHUDConfig extends HUDConfig {
    public Boolean nums_only = true;
    public Boolean show_dirs = true;
    public Boolean show_biome = true;
    public Color bg_color = new Color(0x80000000, true);
    public Color label_color = new Color(0x55FF55);
    public Color number_color = Color.white;
}
