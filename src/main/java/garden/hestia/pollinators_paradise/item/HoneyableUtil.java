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
	public static final String HONEY_NBT_KEY = "HoneyLevel";

	public static boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT && otherStack.isOf(Items.HONEY_BOTTLE) && getHoneyLevel(thisStack) < MAX_HONEY_LEVEL) {
			playInsertSound(player);
			ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1));
			putHoneyLevel(thisStack, Math.min(getHoneyLevel(thisStack) + HONEY_VALUE, MAX_HONEY_LEVEL));
			return true;
		}
		return false;
	}
	private static void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	public static int getItemBarColor(ItemStack stack) {
		return HoneyableUtil.ITEM_BAR_COLOR;
	}

	public static boolean isItemBarVisible(ItemStack stack) {
		return true;
	}


	public static int getItemBarStep(ItemStack stack) {return Math.round(13.0F/(float) HoneyableUtil.MAX_HONEY_LEVEL * (float)  getHoneyLevel(stack));
	}

	public static int getHoneyLevel(ItemStack stack)
	{
		return stack.getNbt() != null ? stack.getNbt().getInt(HoneyableUtil.HONEY_NBT_KEY) : 0;
	}
	public static void putHoneyLevel(ItemStack stack, int newLevel)
	{
		stack.getOrCreateNbt().putInt(HONEY_NBT_KEY, newLevel);
	}
}
