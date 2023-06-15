package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.HoneyableUtil;
import net.minecraft.block.Block;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
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
	public World world;
	@Shadow
	protected abstract BlockPos getVelocityAffectingPos();
	@ModifyVariable(method = "getJumpVelocityMultiplier", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getJumpVelocityMultiplier()F"), index = 1)
	protected float applyHoneyBounce(float original)
	{
		Block block = this.world.getBlockState(this.getVelocityAffectingPos()).getBlock();
		if (block instanceof HoneyBlock)
		{
			if ((Object) this instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.FEET).isOf(PollinatorsParadise.APIARIST_WELLIES)
					&& HoneyableUtil.getHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET)) > 0 && !player.isSneaking())
			{
				HoneyableUtil.putHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET), HoneyableUtil.getHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET)) - 32);
				return 1.8F;
			}
		}
		return original;
	}
	@Inject(method = "applyDamageEffects", at = @At(value = "TAIL"))
	public void applyDamageEffects(LivingEntity attacker, Entity target, CallbackInfo ci)
	{
		if (target instanceof LivingEntity livingTarget && livingTarget.getEquippedStack(EquipmentSlot.CHEST).isOf(PollinatorsParadise.APIARIST_SUIT)
		 && HoneyableUtil.getHoneyLevel(livingTarget.getEquippedStack(EquipmentSlot.CHEST)) > 0)
		{
			HoneyableUtil.putHoneyLevel(livingTarget.getEquippedStack(EquipmentSlot.CHEST), HoneyableUtil.getHoneyLevel(livingTarget.getEquippedStack(EquipmentSlot.CHEST)) - 16);

			attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1), livingTarget);
			if (target.getWorld() instanceof ServerWorld serverWorld)
			{
				serverWorld.getChunkManager().sendToNearbyPlayers(target, new EntityAnimationS2CPacket(attacker, EntityAnimationS2CPacket.CRIT));
			}

		}
	}
}
