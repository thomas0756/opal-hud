package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.config.ServerAddressHUDConfig;
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
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ServerAddressHUD extends HUDElement {

    boolean inEditor = false;
    public ServerAddressHUDConfig config = new ServerAddressHUDConfig();
    
    public ServerAddressHUD() {
        super("Server Address HUD", 0, 0, 103, 15, 0, OpalHUD.MOD_ID, "server-address-hud", HudAnchor.HorizontalAnchor.RIGHT, HudAnchor.VerticalAnchor.TOP);
    }

    @Override
    public void render(int x, int y, int width, int height, GuiGraphics graphics, float delta) {
        Minecraft mc = Minecraft.getInstance();

        String address_string;
        if (inEditor) address_string = "mc.server.address";
        else if (mc.isLocalServer()) address_string = "Local Server";
        else {
            ServerData current_server= mc.getCurrentServer();
            if (current_server == null) address_string = "Unknown";
            else address_string = current_server.ip;
        }

        int address_width = mc.font.width(address_string) + 4;
        int bg_start = x - address_width - ((config.show_labels) ? 44 : 0) - 8;

        graphics.fill(bg_start + width, y, x + width, y + height, utils.ColorToInt(config.bg_color, true));

        graphics.drawString(mc.font, address_string, x - address_width + width, y + 4, utils.ColorToInt(config.text_color, false));

        if (config.show_labels) graphics.drawString(mc.font, "Server:", bg_start + 4 + width, y + 4, utils.ColorToInt(config.label_color, false));
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
        return ServerAddressHUDConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof ServerAddressHUDConfig) {
                this.config = (ServerAddressHUDConfig) config;
            }
        }
    }

    @Override
    public OptionGroup generateConfig() {

        return OptionGroup.createBuilder()
                .name(net.minecraft.network.chat.Component.literal("Server Address HUD"))
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Show Labels"))
                        .binding(false,
                                () -> config.show_labels,
                                newBool -> config.show_labels = newBool)
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
                .option(Option.<Color>createBuilder()
                        .name(Component.literal("Text Color"))
                        .binding(new Color(0xFFFFFF, true),
                                () -> config.text_color,
                                newColor -> config.text_color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .option(Option.<Color>createBuilder()
                        .name(Component.literal("Label Color"))
                        .binding(new Color(0x55FF55, true),
                                () -> config.label_color,
                                newColor -> config.label_color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .build();
    }
}
