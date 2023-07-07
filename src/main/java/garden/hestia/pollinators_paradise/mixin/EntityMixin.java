package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.block.ChorusHoneyBlock;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow
	private World world;
	@Shadow
	protected abstract BlockPos getVelocityAffectingPos();
	@ModifyVariable(method = "getJumpVelocityMultiplier", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getJumpVelocityMultiplier()F"), index = 1)
	@SuppressWarnings("ConstantConditions")
	protected float applyHoneyBounce(float original)
	{
		Block block = this.world.getBlockState(this.getVelocityAffectingPos()).getBlock();
		if (block instanceof ChorusHoneyBlock)
		{
			if ((Object) this instanceof PlayerEntity player)
			{
				ItemStack equippedFeetStack = player.getEquippedStack(EquipmentSlot.FEET);
				if (equippedFeetStack.isOf(PollinatorsParadise.APIARIST_WELLIES) && equippedFeetStack.getItem() instanceof Honeyable honeyItem
						&& player.isSneaking()
						&& honeyItem.decrementHoneyLevel(equippedFeetStack, Honeyable.HoneyType.HONEY))
				{
					return 5.0F;
				}
			}

		}
		return original;
	}
	@Inject(method = "applyDamageEffects", at = @At(value = "TAIL"))
	public void applyDamageEffects(LivingEntity attacker, Entity target, CallbackInfo ci)
	{
		if (target instanceof LivingEntity livingTarget)
		{
			ItemStack equippedChestStack = livingTarget.getEquippedStack(EquipmentSlot.CHEST);
			if (equippedChestStack.isOf(PollinatorsParadise.APIARIST_SUIT)
					&& equippedChestStack.getItem() instanceof Honeyable honeyItem
					&& honeyItem.decrementHoneyLevel(equippedChestStack, Honeyable.HoneyType.HONEY))
			{
				attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1), livingTarget);
				if (target.getWorld() instanceof ServerWorld serverWorld)
				{
					serverWorld.getChunkManager().sendToNearbyPlayers(target, new EntityAnimationS2CPacket(attacker, EntityAnimationS2CPacket.CRIT));
				}

			}
		}

	}
}
