package com.amplicube.opalhud.config;

import dev.wooferz.hudlib.hud.HUDConfig;

import java.awt.*;

public class PingHUDConfig extends HUDConfig {
    public float ping_rate = 5;
    public Boolean num_only = false;
    public Color bg_color = new Color(0x80000000, true);
    public Boolean static_color = true;
    public Color text_color = Color.white;
}
