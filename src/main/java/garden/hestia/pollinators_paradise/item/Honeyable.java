package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

public interface Honeyable {
	int ITEM_BAR_COLOR = 0xff9116;
	String HONEY_NBT_KEY = "HoneyLevel";

	int bottleCapacity();
	int bottlePoints();

	default int pointCapacity()
	{
		return bottleCapacity() * bottlePoints();
	}

	default boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT && otherStack.isOf(Items.HONEY_BOTTLE) && getHoneyLevel(thisStack) < pointCapacity()) {
			playInsertSound(player);
			ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1));
			putHoneyLevel(thisStack, Math.min(getHoneyLevel(thisStack) + bottlePoints(), pointCapacity()));
			return true;
		}
		return false;
	}
	default void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	default int getItemBarColor(ItemStack stack) {
		return Honeyable.ITEM_BAR_COLOR;
	}

	default boolean isItemBarVisible(ItemStack stack) {
		return true;
	}


	default int getItemBarStep(ItemStack stack) {return Math.round(13.0F/(float) pointCapacity() * (float)  getHoneyLevel(stack));
	}

	default int getHoneyLevel(ItemStack stack)
	{
		return stack.getNbt() != null ? Math.min(stack.getNbt().getInt(Honeyable.HONEY_NBT_KEY), pointCapacity())  : 0;
	}
	default void putHoneyLevel(ItemStack stack, int newLevel)
	{
		stack.getOrCreateNbt().putInt(HONEY_NBT_KEY, newLevel);
	}
}
