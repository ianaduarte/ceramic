package dev.ianaduarte.ceramic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

public class Ceramic implements ModInitializer {
	public static final String MOD_ID = "ceramic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static ResourceLocation getLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
	
	@Override
	public void onInitialize() {
	}
}