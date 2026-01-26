package com.amplicube.opalhud;

import com.amplicube.opalhud.huds.CoordsHUD;
import com.amplicube.opalhud.huds.FPSHUD;
import com.amplicube.opalhud.huds.PingHUD;

import com.amplicube.opalhud.huds.ServerAddressHUD;
import dev.wooferz.hudlib.HudManager;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpalHUD implements ClientModInitializer {
	public static final String MOD_ID = "opal-hud";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Registering HUDs with HUD Lib...");
		HudManager.registerHudElement(new FPSHUD());
		HudManager.registerHudElement(new PingHUD());
		HudManager.registerHudElement(new CoordsHUD());
		HudManager.registerHudElement(new ServerAddressHUD());
		LOGGER.info("Done registering HUDs.");
	}
}