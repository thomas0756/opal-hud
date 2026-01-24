package com.amplicube.opalhud.huds;

import com.amplicube.opalhud.OpalHUD;
import com.amplicube.opalhud.config.TestHUDConfig;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.wooferz.hudlib.HudAnchor;
import dev.wooferz.hudlib.hud.HUDConfig;
import dev.wooferz.hudlib.hud.HUDElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TestHUD extends HUDElement {

    boolean inEditor = false;
    public TestHUDConfig config = new TestHUDConfig();
    
    public TestHUD() {
        super("Test HUD", 100, 100, 50, 20, 1, OpalHUD.MOD_ID, "test-hud", HudAnchor.HorizontalAnchor.RIGHT, HudAnchor.VerticalAnchor.MIDDLE);
    }

    @Override
    public void render(int x, int y, int width, int height, GuiGraphics graphics, float v) {
        Minecraft mc = Minecraft.getInstance();
        graphics.fill(x, y, x + width, y + height, 0x80808080);
        graphics.drawString(mc.font, config.text, x + 5, y + 5, 0xffffffff);
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
        OptionGroup optionGroup = OptionGroup.createBuilder()
                .name(Component.literal("Test HUD"))
                .option(Option.<String>createBuilder()
                        .name(Component.literal("Test Text"))
                        .binding("Test",
                                () -> config.text,
                                value -> config.text = value)
                        .controller(StringControllerBuilder::create)
                        .build())
                .build();

        return optionGroup;
    }
}
