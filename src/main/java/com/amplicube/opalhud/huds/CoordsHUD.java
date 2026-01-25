package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.config.CoordsHUDConfig;

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
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;

public class CoordsHUD extends HUDElement {

    boolean inEditor = false;
    CoordsHUDConfig config = new CoordsHUDConfig();
    
    public CoordsHUD() {
        super("Coordinates HUD", 0, 50, 76, 53, 0, OpalHUD.MOD_ID, "coords-hud", HudAnchor.HorizontalAnchor.LEFT, HudAnchor.VerticalAnchor.TOP);
    }

    @Override
    public void render(int hudx, int hudy, int width, int height, GuiGraphics graphics, float delta) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            int x;
            int y;
            int z;

            if (inEditor) {
                x = 1000;
                y = 64;
                z = -1000;
            }
            else {
                x = (int) mc.player.getX();
                y = (int) mc.player.getY();
                z = (int) mc.player.getZ();
            }

            String x_string = String.valueOf(x);
            String y_string = String.valueOf(y);
            String z_string = String.valueOf(z);

            int x_offset = 4;
            int y_offset = 0;


            y_offset += 41;

            int widest_width = Math.max(Math.max(mc.font.width(x_string), mc.font.width(y_string)), mc.font.width(z_string));
            int widest_inc_biome = widest_width;

            Component biome_string = Component.literal("Unknown");
            if (config.show_biome) {
                if (mc.level != null) {
                    Identifier biome_identifier = mc.level.registryAccess().lookupOrThrow(Registries.BIOME).getKey(mc.level.getBiome(new BlockPos(x, y, z)).value());
                    if (biome_identifier != null) biome_string = Component.translatable("biome." + biome_identifier.toLanguageKey());
                }

                widest_inc_biome = Math.max(widest_width, mc.font.width(biome_string.getString()));

                graphics.fill(hudx, hudy + 38, hudx + widest_inc_biome + ((config.nums_only) ? 18 : 0) + 4 + ((config.show_dirs) ? 24 : 0), hudy + 53 , utils.ColorToInt(config.bg_color, true));

                int biome_offset = 0;
                if (config.nums_only) {
                    biome_offset = 36;
                    graphics.drawString(mc.font, "Biome:", hudx + 4, hudy + y_offset, utils.ColorToInt(config.label_color, false));
                }
                graphics.drawString(mc.font, biome_string, hudx + 4 + biome_offset, hudy + y_offset, utils.ColorToInt(config.number_color, false));
            }

            y_offset -= 41;

            graphics.fill(hudx, hudy, hudx + widest_inc_biome + ((config.nums_only) ? 18 : 0) + 4 + ((config.show_dirs) ? 24 : 0), hudy + 38 , utils.ColorToInt(config.bg_color, true));

            if (config.nums_only) {
                x_offset = 18;
                graphics.drawString(mc.font, "X:", hudx + 4, hudy + 4 + y_offset, utils.ColorToInt(config.label_color, false));
                graphics.drawString(mc.font, "Y:", hudx + 4, hudy + 15 + y_offset, utils.ColorToInt(config.label_color, false));
                graphics.drawString(mc.font, "Z:", hudx + 4, hudy + 26 + y_offset, utils.ColorToInt(config.label_color, false));
            }

            graphics.drawString(mc.font, x_string, hudx + x_offset, hudy + 4 + y_offset, utils.ColorToInt(config.number_color, false));
            graphics.drawString(mc.font, y_string, hudx + x_offset, hudy + 15 + y_offset, utils.ColorToInt(config.number_color, false));
            graphics.drawString(mc.font, z_string, hudx + x_offset, hudy + 26 + y_offset, utils.ColorToInt(config.number_color, false));

            x_offset += widest_width + 12;

            if (config.show_dirs) {
                String x_dir = " ";
                String z_dir = " ";
                String compass_dir;

                float facing = mc.player.getForward().rotation().y;

                if (facing >= 157.5) {
                    z_dir = "-";
                    compass_dir = "N";
                } else if (facing >= 112.5) {
                    z_dir = "-";
                    x_dir = "-";
                    compass_dir = "NW";
                } else if (facing >= 67.5) {
                    x_dir = "-";
                    compass_dir = "W";
                } else if (facing >= 22.5) {
                    z_dir = "+";
                    x_dir = "-";
                    compass_dir = "SW";
                } else if (facing >= -22.5) {
                    z_dir = "+";
                    compass_dir = "S";
                } else if (facing >= -67.5) {
                    z_dir = "+";
                    x_dir = "+";
                    compass_dir = "SE";
                } else if (facing >= -112.5) {
                    x_dir = "+";
                    compass_dir = "E";
                } else if (facing >= -157.5) {
                    z_dir = "-";
                    x_dir = "+";
                    compass_dir = "NE";
                } else {
                    z_dir = "-";
                    compass_dir = "N";
                }

                graphics.drawString(mc.font, compass_dir, hudx + x_offset, hudy + 15 + y_offset, utils.ColorToInt(config.number_color, false));
                if (config.nums_only) {
                    graphics.drawString(mc.font, x_dir, hudx + x_offset, hudy + 4 + y_offset, utils.ColorToInt(config.label_color, false));
                    graphics.drawString(mc.font, z_dir, hudx + x_offset, hudy + 26 + y_offset, utils.ColorToInt(config.label_color, false));
                }
                else {
                    graphics.drawString(mc.font, x_dir, hudx + x_offset, hudy + 4 + y_offset, utils.ColorToInt(config.number_color, false));
                    graphics.drawString(mc.font, z_dir, hudx + x_offset, hudy + 26 + y_offset, utils.ColorToInt(config.number_color, false));
                }

                //x_offset += 18;
            }
        }
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
        return CoordsHUDConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof CoordsHUDConfig) {
                this.config = (CoordsHUDConfig) config;
            }
        }
    }

    @Override
    public OptionGroup generateConfig() {

        return OptionGroup.createBuilder()
                .name(Component.literal("Coordinates HUD"))
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Show Labels"))
                        .binding(true,
                                () -> config.nums_only,
                                newBool -> config.nums_only = newBool)
                        .controller(BooleanControllerBuilder::create)
                        .build()
                )
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Show Direction"))
                        .binding(true,
                                () -> config.show_dirs,
                                newBool -> config.show_dirs = newBool)
                        .controller(BooleanControllerBuilder::create)
                        .build()
                )
                .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Show Biome"))
                        .binding(true,
                                () -> config.show_biome,
                                newBool -> config.show_biome = newBool)
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
                        .name(Component.literal("Label Color"))
                        .binding(new Color(0x55FF55, false),
                                () -> config.label_color,
                                newColor -> config.label_color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .option(Option.<Color>createBuilder()
                        .name(Component.literal("Number Color"))
                        .binding(new Color(0xFFFFFF, false),
                                () -> config.number_color,
                                newColor -> config.number_color = newColor)
                        .controller(ColorControllerBuilder::create)
                        .build()
                )
                .build();
    }
}
