package dev.ianaduarte.ceramic.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.ianaduarte.ceramic.renderer.CeramicRendererOverrides;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderers.class)
public class OverrideRenderers {
	@ModifyExpressionValue(
		method = "method_32174",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider;create(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
		)
	)
	private static EntityRenderer<?, ?> redirectFactories(EntityRenderer<?, ?> original, @Local(argsOnly = true) EntityType<?> entityType, @Local(argsOnly = true) EntityRendererProvider.Context context) {
		if(!CeramicRendererOverrides.hasOverride(entityType)) return original;
		return CeramicRendererOverrides.getRendererFactory(entityType).create(context);
	}
}
