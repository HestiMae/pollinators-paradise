package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorLivingEntity;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {

	@Inject(method = "updateSlidingVelocity", at = @At(value = "HEAD"), cancellable = true)
	public void honeyAllowsJumping(Entity entity, CallbackInfo ci)
	{
		if (entity instanceof PlayerEntity player)
		{
			ItemStack equippedLegStack = player.getEquippedStack(EquipmentSlot.LEGS);
			if (equippedLegStack.isOf(PollinatorsParadise.APIARIST_LEGGINGS) && equippedLegStack.getItem() instanceof Honeyable honeyItem
					&& honeyItem.getHoneyLevel(equippedLegStack) > 0)
			{
				if (player instanceof PollinatorLivingEntity pollinatorPlayer && pollinatorPlayer.pollinators$jumping())
				{
					if (pollinatorPlayer.pollinators$jumpCooldown() <= 0)
					{
						pollinatorPlayer.pollinators$wallJump();
						honeyItem.putHoneyLevel(equippedLegStack, honeyItem.getHoneyLevel(equippedLegStack) - 1);
					}
					ci.cancel();
				}
			}
		}
	}
}