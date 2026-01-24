package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.config.TestHUDConfig;

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
    public TestHUDConfig config = new TestHUDConfig();
    
    public FPSHUD() {
        super("FPS HUD", 100, 100, 48, 15, 4, OpalHUD.MOD_ID, "fps-hud", HudAnchor.HorizontalAnchor.RIGHT, HudAnchor.VerticalAnchor.MIDDLE);
    }

    @Override
    public void render(int x, int y, int width, int height, GuiGraphics graphics, float v) {
        Minecraft mc = Minecraft.getInstance();

        String fps_string = String.valueOf(mc.getFps());
        if (!config.num_only) {
            fps_string += " FPS";
        }

        graphics.fill(x, y, x + mc.font.width(fps_string) + padding * 2, y + height, utils.ColorToInt(config.bg_color));
        graphics.drawString(mc.font, fps_string, x + padding, y + padding, utils.ColorToInt(config.text_color));
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
        return TestHUDConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof TestHUDConfig) {
                this.config = (TestHUDConfig) config;
            }
        }
    }

    @Override
    public OptionGroup generateConfig() {
        return OptionGroup.createBuilder()
                .name(Component.literal("FPS HUD"))
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Number Only"))
                        .binding(true,
                                () -> config.num_only,
                                newBool -> config.num_only = newBool)
                        .controller(BooleanControllerBuilder::create)
                        .build()
                ).option(Option.<Color>createBuilder()
                        .name(Component.literal("Background Color"))
                        .binding(new Color(0x80000000, true),
                                () -> config.bg_color,
                                newColor -> config.bg_color = newColor)
                        .controller(opt -> ColorControllerBuilder.create(opt)
                                .allowAlpha(true))
                        .build()
                )
                .option(Option.<Color>createBuilder()
                        .name(Component.literal("Text Color"))
                        .binding(new Color(0xFFFFFFFF, true),
                                () -> config.text_color,
                                newColor -> config.text_color = newColor)
                        .controller(opt -> ColorControllerBuilder.create(opt)
                                .allowAlpha(true))
                        .build()
                )
                .build();
    }
}
