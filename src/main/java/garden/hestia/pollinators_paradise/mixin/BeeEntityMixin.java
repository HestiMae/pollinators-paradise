package garden.hestia.pollinators_paradise.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends LivingEntity {

	protected BeeEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArg(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;<init>(Lnet/minecraft/entity/effect/StatusEffect;II)V"), index = 2)
	private int strengthDoublesPoison(int amplifier)
	{
		return this.hasStatusEffect(StatusEffects.STRENGTH) ? 1 : amplifier;
	}

	@ModifyArg(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;<init>(Lnet/minecraft/entity/effect/StatusEffect;II)V"), index = 0)
	private StatusEffect strengthAppliesWither(StatusEffect originalEffect)
	{
		return (this.hasStatusEffect(StatusEffects.STRENGTH) && this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() >= 1) ? StatusEffects.WITHER : originalEffect;
	}
}
