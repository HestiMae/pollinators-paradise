package garden.hestia.pollinators_paradise.item;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

import static org.joml.Math.clamp;

public interface Honeyable {
	int HONEY_ITEM_BAR_COLOR = 0xff9116;
	int CHORUS_ITEM_BAR_COLOR = 0x9128a3;
	String HONEY_NBT_KEY = "HoneyLevel";
	String HONEY_TYPE_NBT_KEY = "HoneyType";

	enum HoneyType {
		HONEY,
		CHORUS,
		NONE
	}

	int bottleCapacity();

	int bottlePoints();

	default HoneyType getHoneyType(ItemStack stack)
	{
		try
		{
			return HoneyType.valueOf(stack.getOrCreateNbt().getString(HONEY_TYPE_NBT_KEY));
		} catch (IllegalArgumentException illegalArgumentException)
		{
			return HoneyType.NONE;
		}
	}

	default int pointCapacity() {
		return bottleCapacity() * bottlePoints();
	}

	default boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT && otherStack.isOf(Items.HONEY_BOTTLE) && addHoney(thisStack, bottlePoints(), HoneyType.HONEY)) {
			playInsertSound(player);
			ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1));
			return true;
		} else if (clickType == ClickType.RIGHT && otherStack.isOf(PollinatorsParadise.CHORUS_HONEY_BOTTLE) && addHoney(thisStack, bottlePoints(), HoneyType.CHORUS)) {
			playInsertSound(player);
			ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1));
			return true;
		} else if (clickType == ClickType.RIGHT && PotionUtil.getPotion(otherStack) == Potions.WATER && getHoneyType(thisStack) != HoneyType.NONE) {
			playInsertSound(player);
			ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1));
			putHoneyLevel(thisStack, 0, HoneyType.NONE);
			return true;
		}
		return false;
	}

	default void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	default int getItemBarColor(ItemStack stack) {
		return switch (getHoneyType(stack)) {
			case HONEY -> Honeyable.HONEY_ITEM_BAR_COLOR;
			case CHORUS -> Honeyable.CHORUS_ITEM_BAR_COLOR;
			case NONE -> 0x000000;
		};
	}

	default boolean isItemBarVisible(ItemStack stack) {
		return true;
	}


	default int getItemBarStep(ItemStack stack) {
		return Math.round(13.0F / (float) pointCapacity() * (float) getHoneyLevel(stack, getHoneyType(stack)));
	}

	default int getItemTintColor(ItemStack stack)
	{
		return switch (getHoneyType(stack)) {
			case HONEY -> Honeyable.HONEY_ITEM_BAR_COLOR;
			case CHORUS -> Honeyable.CHORUS_ITEM_BAR_COLOR;
			case NONE -> -1;
		};
	}

	default float[] getArmorColor(ItemStack stack)
	{
		return switch (getHoneyType(stack)) {
			case HONEY -> new float[]{ColorUtil.ARGB32.getRed(Honeyable.HONEY_ITEM_BAR_COLOR), ColorUtil.ARGB32.getGreen(Honeyable.HONEY_ITEM_BAR_COLOR), ColorUtil.ARGB32.getBlue(Honeyable.HONEY_ITEM_BAR_COLOR)};
			case CHORUS -> new float[]{ColorUtil.ARGB32.getRed(Honeyable.CHORUS_ITEM_BAR_COLOR), ColorUtil.ARGB32.getGreen(Honeyable.CHORUS_ITEM_BAR_COLOR), ColorUtil.ARGB32.getBlue(Honeyable.CHORUS_ITEM_BAR_COLOR)};
			case NONE -> new float[]{};
		};
	}

	default int getHoneyLevel(ItemStack stack, HoneyType type) {
		return getHoneyType(stack) == type ?  clamp(0, stack.getOrCreateNbt().getInt(Honeyable.HONEY_NBT_KEY), pointCapacity()) : 0;
	}

	default int getHoneyQuartile(ItemStack stack, HoneyType type) {
		return (int) Math.ceil(4 * ((float) getHoneyLevel(stack, type)) / pointCapacity());
	}

	default void putHoneyLevel(ItemStack stack, int newLevel, HoneyType type) {
		stack.getOrCreateNbt().putInt(HONEY_NBT_KEY, clamp(0, newLevel, pointCapacity()));
		stack.getOrCreateNbt().putString(HONEY_TYPE_NBT_KEY, String.valueOf(newLevel <= 0 ? HoneyType.NONE : type));
	}

	default boolean decrementHoneyLevel(ItemStack stack, HoneyType type) {
		return decrementHoneyLevel(stack,1, type);
	}

	default boolean decrementHoneyLevel(ItemStack stack, int amount, HoneyType type) {
		int honeyLevel = getHoneyLevel(stack, type);
		if (honeyLevel > 0)
		{
			putHoneyLevel(stack, honeyLevel - amount, type);
		}
		return honeyLevel > 0;
	}

	default boolean addHoney(ItemStack stack, int amount, HoneyType type) {
		if ((getHoneyType(stack) == type || getHoneyType(stack) == HoneyType.NONE) && getHoneyLevel(stack, type) < pointCapacity()) {
			putHoneyLevel(stack, getHoneyLevel(stack, type) + amount, type);
			return true;
		}
		return false;
	}
}
