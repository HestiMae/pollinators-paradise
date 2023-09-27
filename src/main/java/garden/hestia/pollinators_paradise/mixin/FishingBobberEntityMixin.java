package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsItems;
import garden.hestia.pollinators_paradise.entity.ApiaristFishingBobberEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
	@ModifyArg(method = "removeIfInvalid", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
	private Item apiaristIsOf(Item item)
	{
		FishingBobberEntity self = (FishingBobberEntity) (Object) this;
		if (self instanceof ApiaristFishingBobberEntity)
		{
			return PollinatorsItems.APIARIST_FISHING_ROD;
		}
		return item;
	}
}
