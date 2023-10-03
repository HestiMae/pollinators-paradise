package garden.hestia.pollinators_paradise.addon.mixin;

import garden.hestia.pollinators_paradise.addon.HoneyTypes;
import garden.hestia.pollinators_paradise.addon.PollinatorLivingEntity;
import garden.hestia.pollinators_paradise.addon.PollinatorsItems;
import garden.hestia.pollinators_paradise.addon.client.PollinatorsClientNetworking;
import garden.hestia.pollinators_paradise.base.Honeyable;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.Stainable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoneyBlock.class)
public abstract class HoneyBlockMixin implements Stainable {

	@Inject(method = "updateSlidingVelocity", at = @At(value = "HEAD"), cancellable = true)
	public void honeyAllowsJumping(Entity entity, CallbackInfo ci) {
		if (entity instanceof PlayerEntity player) {
			ItemStack equippedLegStack = player.getEquippedStack(EquipmentSlot.LEGS);
			if (equippedLegStack.isOf(PollinatorsItems.APIARIST_LEGGINGS) && equippedLegStack.getItem() instanceof Honeyable honeyItem) {
				if (player instanceof PollinatorLivingEntity pollinatorPlayer && pollinatorPlayer.pollinators$jumping()) {
					if (pollinatorPlayer.pollinators$jumpCooldown() <= 0 && honeyItem.getHoneyLevel(equippedLegStack, HoneyTypes.HONEY) > 0) {
						if (entity.getWorld().isClient()) {
							PollinatorsClientNetworking.wallJump();
						}
						pollinatorPlayer.pollinators$wallJump();
					}
					ci.cancel();
				}
			}
		}
	}

	@Override
	public DyeColor getColor() {
		return DyeColor.ORANGE;
	}
}
