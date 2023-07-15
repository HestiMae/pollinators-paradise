package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorEntity;
import garden.hestia.pollinators_paradise.PollinatorPlayerEntity;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.block.ChorusHoneyBlock;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.Block;
import net.minecraft.block.HoneyBlock;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements PollinatorEntity {

	@Unique
	private Block crystallised = null;

	@Shadow
	protected abstract BlockPos getVelocityAffectingPos();

	@Shadow
	public abstract World getWorld();

	@Inject(at=@At("TAIL"), method="baseTick")
	protected void baseTick(CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		this.crystallised = null;
		if (self instanceof PlayerEntity player) {
			Block honeyBlock = getAffectingHoneyBlock();
			if (honeyBlock != null) {
				ItemStack equippedFeetStack = player.getEquippedStack(EquipmentSlot.FEET);
				if (equippedFeetStack.isOf(PollinatorsParadise.APIARIST_WELLIES) && equippedFeetStack.getItem() instanceof Honeyable honeyItem
						&& player.isSneaking()
						&& honeyItem.getHoneyType(equippedFeetStack) == Honeyable.HoneyType.HONEY) {
					crystallised = honeyBlock;
				}
			}
		}
		if (crystallised != null)
		{
			self.fallDistance = 0;
			self.setVelocity(self.getVelocity().x, 0, self.getVelocity().z);
			if (self instanceof PlayerEntity playerEntity && !playerEntity.hasStatusEffect(StatusEffects.RESISTANCE) && !(crystallised instanceof ChorusHoneyBlock)
					&& playerEntity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof Honeyable honeyItem && honeyItem.decrementHoneyLevel(playerEntity.getEquippedStack(EquipmentSlot.FEET), 2, Honeyable.HoneyType.HONEY)) {
				playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, true, true), playerEntity);
			}
		}
		if (self instanceof PollinatorPlayerEntity pollinatorPlayerEntity && pollinatorPlayerEntity.getWelliesMount().jumpStrength > 0)
		{
			self.setVelocity(self.getVelocity().x, pollinatorPlayerEntity.getWelliesMount().jumpStrength, self.getVelocity().z);
			pollinatorPlayerEntity.getWelliesMount().jumpStrength = 0;
		}
	}

	@Inject(method = "applyDamageEffects", at = @At(value = "TAIL"))
	public void applyDamageEffects(LivingEntity attacker, Entity target, CallbackInfo ci) {
		if (target instanceof LivingEntity livingTarget) {
			ItemStack equippedChestStack = livingTarget.getEquippedStack(EquipmentSlot.CHEST);
			if (equippedChestStack.isOf(PollinatorsParadise.APIARIST_SUIT)
					&& equippedChestStack.getItem() instanceof Honeyable honeyItem
					&& honeyItem.decrementHoneyLevel(equippedChestStack, Honeyable.HoneyType.HONEY)) {
				attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1), livingTarget);
				if (target.getWorld() instanceof ServerWorld serverWorld) {
					serverWorld.getChunkManager().sendToNearbyPlayers(target, new EntityAnimationS2CPacket(attacker, EntityAnimationS2CPacket.CRIT));
				}

			}
		}

	}

	@Inject(at=@At("RETURN"), method="getJumpVelocityMultiplier", cancellable=true)
	protected void getJumpVelocityMultiplier(CallbackInfoReturnable<Float> ci) {
		if (crystallised != null) ci.setReturnValue(0f);
	}

	@Inject(at=@At("RETURN"), method="getVelocityMultiplier", cancellable=true)
	protected void getVelocityMultiplier(CallbackInfoReturnable<Float> ci) {
		if (crystallised != null) ci.setReturnValue(ci.getReturnValueF()*0.1f);
	}


	private Block getAffectingHoneyBlock()
	{
		Block block = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock();
		return block instanceof HoneyBlock ? block : null;
	}
	@Override
	public Block getCrystallised()
	{
		return crystallised;
	}
}
