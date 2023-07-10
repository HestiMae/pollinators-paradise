package garden.hestia.pollinators_paradise.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends LivingEntity {

	protected BeeEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	@Shadow
	protected abstract void setHasStung(boolean hasStung);

	@ModifyArg(method = "mobTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BeeEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 1), index = 1)
	protected float regenerateStinger(float amount) {
		if (this.hasStatusEffect(StatusEffects.RESISTANCE) || this.hasStatusEffect(StatusEffects.REGENERATION))
		{
			this.setHasStung(false);
			return 1.0F;
		}
		return amount;
	}

	@ModifyArg(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;<init>(Lnet/minecraft/entity/effect/StatusEffect;II)V"), index = 2)
	private int strengthDoublesPoison(int amplifier)
	{
		return this.hasStatusEffect(StatusEffects.STRENGTH) ? 1 : amplifier;
	}

	@SuppressWarnings("DataFlowIssue")
	@ModifyArg(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;<init>(Lnet/minecraft/entity/effect/StatusEffect;II)V"), index = 0)
	private StatusEffect strengthAppliesWither(StatusEffect originalEffect)
	{
		return (this.hasStatusEffect(StatusEffects.STRENGTH) && this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() >= 1) ? StatusEffects.WITHER : originalEffect;
	}

	@ModifyVariable(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"), ordinal = 0)
	private int beePoisonOnEasy(int original, Entity target)
	{
		if (target instanceof PlayerEntity || this.getWorld().getDifficulty() == Difficulty.PEACEFUL)
		{
			return original;
		}
		return 5;
	}
}
