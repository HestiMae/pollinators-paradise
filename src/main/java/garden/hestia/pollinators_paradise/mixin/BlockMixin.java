package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.HoneyableUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
	public BlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "onSteppedOn", at = @At(value = "TAIL"))
	public void honeyOnSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci)
	{
		if ((Object) this instanceof HoneyBlock) {
			if (entity instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.FEET).isOf(PollinatorsParadise.APIARIST_WELLIES)
					&& HoneyableUtil.getHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET)) > 0 && player.isSneaking() && !player.hasStatusEffect(StatusEffects.RESISTANCE)) {
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, true, true), null);
				HoneyableUtil.putHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET), HoneyableUtil.getHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET)) - 4);
			}
		}
	}
	@Inject(method = "getJumpVelocityMultiplier", at = @At("RETURN"))
	public void getJumpVelocityMultiplier(CallbackInfoReturnable<Float> cir)
	{

	}
}
