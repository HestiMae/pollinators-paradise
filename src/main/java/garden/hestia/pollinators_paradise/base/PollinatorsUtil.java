package garden.hestia.pollinators_paradise.base;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class PollinatorsUtil {
	public static boolean isStackInInventory(ItemStack stack, @Nullable LivingEntity entity) {
		if (entity == null) return false;
		for (ItemStack itemStack : entity.getItemsEquipped()) {
			if (itemStack == stack) return true;
		}
		if (entity instanceof PlayerEntity player) {
			return player.getInventory().main.contains(stack);
		}
		return false;
	}
}
