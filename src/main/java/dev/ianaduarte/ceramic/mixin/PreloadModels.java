package dev.ianaduarte.ceramic.mixin;

import dev.ianaduarte.ceramic.layers.CeramicModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class PreloadModels {
	@Shadow @Final private ReloadableResourceManager resourceManager;
	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V",
			shift = At.Shift.BEFORE,
			ordinal = 0
		)
	)
	public void injectCustomReloadListeners(GameConfig gameConfig, CallbackInfo ci) {
		resourceManager.registerReloadListener(new CeramicModelLayers());
	}
}
