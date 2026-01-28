package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.Utils;
import com.amplicube.opalhud.config.ReachDistanceHUDConfig;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

import java.awt.*;

public class ReachDistanceHUD extends HUDElement {

    boolean inEditor = false;
    public ReachDistanceHUDConfig config = new ReachDistanceHUDConfig();

    static String last_distance = "0.00";

    public ReachDistanceHUD() {
        super("Reach Distance HUD", 0, 0, 27, 15, 0, OpalHUD.MOD_ID, "reach-distance-hud", HudAnchor.HorizontalAnchor.CENTER, HudAnchor.VerticalAnchor.TOP);
    }

    @Override
    public void render(int x, int y, int width, int height, GuiGraphics graphics, float v) {
        Minecraft mc = Minecraft.getInstance();

        if (inEditor) last_distance = "0.00";

        String dist_string;
        if (!config.num_only) dist_string = last_distance + " Blocks";
        else dist_string = last_distance;

        graphics.fill(x, y, x + mc.font.width(dist_string) + 7, y + height, Utils.ColorToInt(config.bg_color, true));
        graphics.drawString(mc.font, dist_string, x + 4, y + 4, Utils.ColorToInt(config.text_color, false));
    }

    public static InteractionResult updateLastDistance(Player player, Level level, InteractionHand ignoredInteractionHand, Entity entity, @Nullable EntityHitResult ignoredEntityHitResult) {
        if (level.isClientSide()){
            if (entity != null) {
                Vec3 player_eyes = player.getEyePosition();

                if (Minecraft.getInstance().hitResult != null) {
                    Vec3 entity_point = Minecraft.getInstance().hitResult.getLocation();
                    last_distance = String.format("%.2f", entity_point.distanceTo(player_eyes));
                }
            }
        }
        return InteractionResult.PASS;
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
        return ReachDistanceHUDConfig.class;
    }

    @Override
    public HUDConfig getConfig() {
        return config;
    }

    @Override
    public void setConfig(HUDConfig config) {
        if (config != null) {
            if (config instanceof ReachDistanceHUDConfig) {
                this.config = (ReachDistanceHUDConfig) config;
            }
        }
    }

    @Override
    public OptionGroup generateConfig() {

        return OptionGroup.createBuilder()
                .name(Component.literal("Reach Distance HUD"))
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
