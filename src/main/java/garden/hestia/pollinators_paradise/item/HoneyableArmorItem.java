package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class HoneyableArmorItem extends ArmorItem implements Honeyable {
	private final int bottleCapacity;
	private final int bottlePoints;

	public HoneyableArmorItem(ArmorMaterial material, ArmorSlot slot, Settings settings, int bottleCapacity, int bottlePoints) {
		super(material, slot, settings);
		this.bottleCapacity = bottleCapacity;
		this.bottlePoints = bottlePoints;
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		return Honeyable.super.onClicked(thisStack, otherStack, thisSlot, clickType, player, cursorStackReference);
	}
	@Override
	public int getItemBarColor(ItemStack stack) {
		return Honeyable.super.getItemBarColor(stack);
	}
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return Honeyable.super.isItemBarVisible(stack);
	}

	@Override
	public int getItemBarStep(ItemStack stack) {return Honeyable.super.getItemBarStep(stack);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public int bottleCapacity() {
		return bottleCapacity;
	}

	@Override
	public int bottlePoints() {
		return bottlePoints;
	}
}
