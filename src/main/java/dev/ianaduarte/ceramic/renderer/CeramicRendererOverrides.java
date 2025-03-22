package dev.ianaduarte.ceramic.renderer;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class CeramicRendererOverrides {
	private static final Map<EntityType<?>, EntityRendererProvider<?>> PROVIDER_OVERRIDES = new Object2ObjectOpenHashMap<>();
	
	public static <T extends Entity> void register(EntityType<T> entityType, EntityRendererProvider<T> provider) {
		PROVIDER_OVERRIDES.put(entityType, provider);
	}
	public static boolean hasOverride(EntityType<?> entityType) {
		return PROVIDER_OVERRIDES.containsKey(entityType);
	}
	public static EntityRendererProvider<?> getRendererFactory(EntityType<?> entityType) {
		return PROVIDER_OVERRIDES.get(entityType);
	}
}
