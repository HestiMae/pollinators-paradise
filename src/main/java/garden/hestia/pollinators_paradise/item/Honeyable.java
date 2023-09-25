package garden.hestia.pollinators_paradise.item;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import garden.hestia.pollinators_paradise.HoneyType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Set;

import static org.joml.Math.clamp;

public interface Honeyable {

	String HONEY_NBT_KEY = "HoneyLevel";
	String HONEY_TYPE_NBT_KEY = "HoneyType";

	int bottleCapacity();

	int bottlePoints();

	default HoneyType getHoneyType(ItemStack stack) {
		try {
			return HoneyType.ofName(stack.getOrCreateNbt().getString(HONEY_TYPE_NBT_KEY));
		} catch (IllegalArgumentException illegalArgumentException) {
			return null;
		}
	}

	default int pointCapacity() {
		return bottleCapacity() * bottlePoints();
	}

	default boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		for (HoneyType type : HoneyType.values()) {
			if (clickType == ClickType.RIGHT && otherStack.isOf(type.bottleItem()) && addHoney(thisStack, bottlePoints(), type)) {
				playInsertSound(player);
				cursorStackReference.set(ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1)));
				return true;
			}
		}

		if (clickType == ClickType.RIGHT && PotionUtil.getPotion(otherStack) == Potions.WATER && getHoneyType(thisStack) != null) {
			playInsertSound(player);
			cursorStackReference.set(ItemUsage.exchangeStack(otherStack, player, new ItemStack(Items.GLASS_BOTTLE, 1)));
			putHoneyLevel(thisStack, 0, null);
			return true;
		}
		return false;
	}

	default void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}

	default int getItemBarColor(ItemStack stack) {
		return getHoneyType(stack) == null ? 0xcec7b9 : getHoneyType(stack).itemBarColor();
	}

	default boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	default int getItemBarStep(ItemStack stack) {
		return Math.round(13.0F / (float) pointCapacity() * (float) getHoneyLevel(stack, getHoneyType(stack)));
	}

	default int getItemTintColor(ItemStack stack) {
		return getHoneyType(stack) == null ? -1 : getHoneyType(stack).itemTintColor();
	}

	default float[] getArmorColor(ItemStack stack) {
		return getHoneyType(stack) == null ? new float[]{} : getHoneyType(stack).armorColor();
	}

	default int getHoneyLevel(ItemStack stack, HoneyType type) {
		return getHoneyType(stack) == type && getHoneyType(stack) != null ? clamp(0, stack.getOrCreateNbt().getInt(Honeyable.HONEY_NBT_KEY), pointCapacity()) : 0;
	}

	default int getHoneyQuartile(ItemStack stack, HoneyType type) {
		return (int) Math.ceil(4 * ((float) getHoneyLevel(stack, type)) / pointCapacity());
	}

	default void putHoneyLevel(ItemStack stack, int newLevel, HoneyType type) {
		stack.getOrCreateNbt().putInt(HONEY_NBT_KEY, clamp(0, newLevel, pointCapacity()));
		if (newLevel > 0)
		{
			stack.getOrCreateNbt().putString(HONEY_TYPE_NBT_KEY, HoneyType.getName(type));
		}
		else {
			stack.getOrCreateNbt().remove(HONEY_TYPE_NBT_KEY);
		}
	}

	default boolean decrementHoneyLevel(ItemStack stack, HoneyType type) {
		return decrementHoneyLevel(stack, 1, type);
	}

	default boolean decrementHoneyLevel(ItemStack stack, int amount, HoneyType type) {
		int honeyLevel = getHoneyLevel(stack, type);
		if (honeyLevel > 0) {
			putHoneyLevel(stack, honeyLevel - amount, type);
		}
		return honeyLevel > 0;
	}

	default boolean addHoney(ItemStack stack, int amount, HoneyType type) {
		if ((getHoneyType(stack) == type || getHoneyType(stack) == null) && getHoneyLevel(stack, type) < pointCapacity()) {
			putHoneyLevel(stack, getHoneyLevel(stack, type) + amount, type);
			return true;
		}
		return false;
	}

	static Multiset<HoneyType> getEquippedHoneyQuarters(@Nullable LivingEntity entity, Set<HoneyType> decrement) {
		Multiset<HoneyType> outSet = TreeMultiset.create(Comparator.comparingInt(HoneyType.values().stream().toList()::indexOf));
		if (entity != null) {
			for (ItemStack stack : entity.getItemsEquipped()) {
				if (stack.getItem() instanceof Honeyable honeyItem) {
					HoneyType type = honeyItem.getHoneyType(stack);
					if (type != null) {
						outSet.add(type, honeyItem.getHoneyQuartile(stack, type));
						if (decrement.contains(type)) honeyItem.decrementHoneyLevel(stack, type);
					}
				}
			}
		}
		return outSet;
	}

	static Multiset<HoneyType> getEquippedHoneyQuarters(@Nullable LivingEntity entity) {
		return getEquippedHoneyQuarters(entity, Set.of());
	}
}
