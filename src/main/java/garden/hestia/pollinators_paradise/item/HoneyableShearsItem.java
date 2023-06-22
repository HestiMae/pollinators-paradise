package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class HoneyableShearsItem extends ShearsItem implements Honeyable {
	private int bottleCapacity;
	private int bottlePoints;

	public HoneyableShearsItem(Settings settings, int bottleCapacity, int bottlePoints) {
		super(settings);
		this.bottleCapacity = bottleCapacity;
		this.bottlePoints = bottlePoints;
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		return onClicked(thisStack, otherStack, thisSlot, clickType, player);
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
	public int bottleCapacity() {
		return bottleCapacity;
	}

	@Override
	public int bottlePoints() {
		return bottlePoints;
	}
}