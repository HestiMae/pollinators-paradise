package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isOf(Item item);

	@Inject(at = @At("HEAD"), method = "isOf", cancellable = true)
	private void isApiaristShears(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item == Items.SHEARS) {
			if (isOf(PollinatorsParadise.APIARIST_SHEARS)) {
				cir.setReturnValue(true);
			}
		}
	}
}
