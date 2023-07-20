package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.HoneyTypes;
import garden.hestia.pollinators_paradise.PollinatorLivingEntity;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.client.PollinatorsParadiseClient;
import garden.hestia.pollinators_paradise.item.Honeyable;
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
			if (equippedLegStack.isOf(PollinatorsParadise.APIARIST_LEGGINGS) && equippedLegStack.getItem() instanceof Honeyable honeyItem) {
				if (player instanceof PollinatorLivingEntity pollinatorPlayer && pollinatorPlayer.pollinators$jumping()) {
					if (pollinatorPlayer.pollinators$jumpCooldown() <= 0 && honeyItem.getHoneyLevel(equippedLegStack, HoneyTypes.HONEY) > 0) {
						if (entity.getWorld().isClient()) {
							PollinatorsParadiseClient.walljump();
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
