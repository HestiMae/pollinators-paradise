package garden.hestia.pollinators_paradise.base.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {

	@Mutable
	@Shadow
	@Final
	private static Predicate<ItemStack> POTION_TYPE_PREDICATE;

	@Inject(method = "<clinit>", at = @At(value = "TAIL"))
	private static void potionPredicate(CallbackInfo ci) {
		POTION_TYPE_PREDICATE = POTION_TYPE_PREDICATE.or(itemStack -> itemStack.isOf(Items.HONEY_BOTTLE));
	}
}
