package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsItems;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.entity.ApiaristFishingBobberEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin {
	@ModifyArgs(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public void spawnApiaristBobberEntity(Args args, World world, PlayerEntity user, Hand hand)
	{
		ItemStack stackInHand = user.getStackInHand(hand);
		if (stackInHand.isOf(PollinatorsItems.APIARIST_FISHING_ROD))
		{
			PollinatorsParadise.LOGGER.info("Yeah! The Final Spawning The Fishing Bobber!");
			args.set(0, new ApiaristFishingBobberEntity(user, world, EnchantmentHelper.getLuckOfTheSea(stackInHand), EnchantmentHelper.getLure(stackInHand)));
		}
	}
}
