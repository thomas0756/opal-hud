package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.config.PingHUDConfig;
import com.amplicube.opalhud.utils;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;

import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import dev.wooferz.hudlib.HudAnchor;
import dev.wooferz.hudlib.hud.HUDConfig;
import dev.wooferz.hudlib.hud.HUDElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PingHUD extends HUDElement {

    boolean inEditor = false;

    public PingHUDConfig config = new PingHUDConfig();

    public PingHUD() {
        super("Ping HUD", 0, 15, 38, 15, 0, OpalHUD.MOD_ID, "ping-hud", HudAnchor.HorizontalAnchor.LEFT, HudAnchor.VerticalAnchor.TOP);
    }

    long ping = 0;
    float total_delta = 99999;

    @Override
    public void render(int x, int y, int width, int height, GuiGraphics graphics, float delta) {
        Minecraft mc = Minecraft.getInstance();

        total_delta += delta;
        if (total_delta > config.ping_rate * 20) {
            total_delta = 0;

            ServerData current_server = mc.getCurrentServer();
            if (current_server != null) {
                String host = current_server.ip;
                int port = 25565;
                int timeout = 5000;
                long before_time = System.currentTimeMillis();
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), timeout);
                    ping = System.currentTimeMillis() - before_time;
                } catch (IOException e) {
                    OpalHUD.LOGGER.error("Couldn't ping server", e);
                }
            }
        }

        String ping_string = String.valueOf(ping);

        if (inEditor) ping_string = "100";

        if (!config.num_only) ping_string += "ms";


        Color text_color;

        if (!config.static_color) {
            if (mc.isLocalServer()) text_color = Color.white;
            else if (ping >= 200) text_color = new Color(0xFF5555);
            else if (ping >= 100) text_color = new Color(0xFFFF00);
            else text_color = new Color(0x55FF55);
        }
        else {
            text_color = config.text_color;
        }

        graphics.fill(x, y, x + mc.font.width(ping_string) + 8, y + height, utils.ColorToInt(config.bg_color, true));
        graphics.drawString(mc.font, ping_string, x + 4, y + 4, utils.ColorToInt(text_color, false));
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
        return PingHUDConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof PingHUDConfig) {
                this.config = (PingHUDConfig) config;
            }
        }
    }

    @Override
    public OptionGroup generateConfig() {

        return OptionGroup.createBuilder()
                .name(net.minecraft.network.chat.Component.literal("Ping HUD"))
                .option(Option.<Float>createBuilder()
                        .name(Component.literal("Ping Rate (Seconds)"))
                        .binding(5f,
                                () -> config.ping_rate,
                                newFloat -> config.ping_rate = newFloat)
                        .controller(opt -> FloatFieldControllerBuilder.create(opt)
                                .min(1f))
                        .build()
                )
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
                        .binding(new Color(0xFFFFFF, true),
                                () -> config.text_color,
                                newColor -> config.text_color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .build();
    }
}

