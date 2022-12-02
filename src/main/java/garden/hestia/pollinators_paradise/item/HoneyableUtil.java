package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

public class HoneyableUtil {
	public static final int MAX_HONEY_LEVEL = 64;
	public static final int HONEY_VALUE = 16;
	public static final int ITEM_BAR_COLOR = 0xff9116;

	public static int onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, int initialHoneyLevel) {
		if (clickType == ClickType.RIGHT && otherStack.isOf(Items.HONEY_BOTTLE) && initialHoneyLevel < MAX_HONEY_LEVEL) {
			playInsertSound(player);
			ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1));
			return Math.min(initialHoneyLevel + HONEY_VALUE, MAX_HONEY_LEVEL);
		}
		return initialHoneyLevel;
	}
	private static void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
}
