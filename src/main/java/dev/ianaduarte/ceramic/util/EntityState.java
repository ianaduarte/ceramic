package dev.ianaduarte.ceramic.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;

public enum EntityState {
	STANDING,
	WALKING,
	SPRINTING,
	
	FLOATING,
	WADING,
	SWIMMING,
	
	PRONING,
	CRAWLING,
	
	CROUCHING,
	SNEAKING,
	
	FALLING,
	GLIDING,
	
	HANGING,
	CLIMBING,
	
	//JUMPING,
	HOVERING,
	FLYING,
	
	DYING,
	RIDING;
	
	private static boolean entityImmersed(Entity entity) {
		return entity.level().getFluidState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ())).getType() != Fluids.EMPTY;
	}
	private static boolean entityFlying(Entity entity) {
		return entity.noPhysics || (entity instanceof FlyingAnimal flyingAnimal && flyingAnimal.isFlying()) || entity instanceof FlyingMob || (entity instanceof Player player && player.getAbilities().flying);
	}
	
	public static EntityState getState(Entity entity) {
		boolean onGround    = entity.onGround();
		boolean isCrouching = entity.isCrouching();
		boolean isCrawling  = entity.isVisuallyCrawling();
		boolean isMoving    = entity.getDeltaMovement().horizontalDistanceSqr() >= 0.001;
		boolean inFluid     = entity.isInLiquid();
		boolean isImmersed  = entityImmersed(entity);
		boolean isFlying    = entityFlying(entity);
		
		if(entity.isPassenger()) return RIDING;
		if(entity instanceof LivingEntity livingEntity) {
			if(livingEntity.isDeadOrDying()) return DYING;
			if(livingEntity.isFallFlying()) return GLIDING;
			if(livingEntity.onClimbable() ) return entity.getDeltaMovement().y != 0 ? CLIMBING : HANGING;
		}
		if(inFluid) {
			if(isImmersed && isMoving && !onGround) return SWIMMING;
			return isMoving || onGround? WADING : FLOATING;
		}
		if(!onGround   ) return isFlying? (isMoving  ? FLYING   : HOVERING) : FALLING;
		if( isCrawling ) return isMoving? CRAWLING : PRONING;
		if( isCrouching) return isMoving? SNEAKING : CROUCHING;
		if( isMoving   ) return entity.isSprinting() ? SPRINTING : WALKING;
		return STANDING;
	}
}
