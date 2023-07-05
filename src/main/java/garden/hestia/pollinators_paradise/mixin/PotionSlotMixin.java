package garden.hestia.pollinators_paradise.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandScreenHandler.PotionSlot.class)
public abstract class PotionSlotMixin {

	@Inject(method = "matches", at = @At(value = "HEAD"), cancellable = true)
	private static void matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
	{
		if (stack.isOf(Items.HONEY_BOTTLE))
		{
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
