package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.config.FPSHUDConfig;

import com.amplicube.opalhud.utils;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;

import dev.wooferz.hudlib.HudAnchor;
import dev.wooferz.hudlib.hud.HUDConfig;
import dev.wooferz.hudlib.hud.HUDElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class FPSHUD extends HUDElement {

    boolean inEditor = false;
    public FPSHUDConfig config = new FPSHUDConfig();
    
    public FPSHUD() {
        super("FPS HUD", 0, 0, 48, 15, 0, OpalHUD.MOD_ID, "fps-hud", HudAnchor.HorizontalAnchor.LEFT, HudAnchor.VerticalAnchor.TOP);
    }

    @Override
    public void render(int x, int y, int width, int height, GuiGraphics graphics, float v) {
        Minecraft mc = Minecraft.getInstance();

        int current_fps = mc.getFps();

        if (inEditor) {
            current_fps = 999;
        }

        Color text_color;

        String fps_string = String.valueOf(current_fps);
        if (!config.num_only) fps_string += " FPS";

        if (!config.static_color) {
            if (current_fps >= 110) text_color = new Color(0x008000);
            else if (current_fps >= 55) text_color = new Color(0x55FF55);
            else if (current_fps >= 28) text_color = new Color(0xFFFF00);
            else text_color = new Color(0xFF5555);
        }
        else {
            text_color = config.text_color;
        }

        graphics.fill(x, y, x + mc.font.width(fps_string) + 8, y + height, utils.ColorToInt(config.bg_color, true));
        graphics.drawString(mc.font, fps_string, x + 4, y + 4, utils.ColorToInt(text_color, false));
    }

    @Override
    public void editorClosed() {
        inEditor = false;
    }

    @Override
    public void editorOpened() {
        inEditor = true;
    }

    @Override
    public Class<?> getConfigType() {
        return FPSHUDConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof FPSHUDConfig) {
                this.config = (FPSHUDConfig) config;
            }
        }
    }

    @Override
    public OptionGroup generateConfig() {

        return OptionGroup.createBuilder()
                .name(Component.literal("FPS HUD"))
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Number Only"))
                        .binding(false,
                                () -> config.num_only,
                                newBool -> config.num_only = newBool)
                        .controller(BooleanControllerBuilder::create)
                        .build()
                )
                .option(Option.<Color>createBuilder()
                        .name(Component.literal("Background Color"))
                        .binding(new Color(0x80000000, true),
                                () -> config.bg_color,
                                newColor -> config.bg_color = newColor)
                        .controller(opt -> ColorControllerBuilder.create(opt)
                                .allowAlpha(true))
                        .build()
                )
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Static Text Color"))
                        .binding(true,
                                () -> config.static_color,
                                newBool -> config.static_color = newBool)
                        .controller(BooleanControllerBuilder::create)
                        .build()
                )
                .option(Option.<Color>createBuilder()
                        .name(Component.literal("Text Color"))
                        .binding(new Color(0xFFFFFF, false),
                                () -> config.text_color,
                                newColor -> config.text_color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .build();
    }
}
