package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class HoneyableItem extends Item {
	private int honeyLevel;
	public HoneyableItem(Settings settings) {
		super(settings);
	}
	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		int initialHoneyLevel = this.honeyLevel;
		this.honeyLevel = HoneyableUtil.onClicked(thisStack, otherStack, thisSlot, clickType, player, this.honeyLevel);
		return this.honeyLevel != initialHoneyLevel;
	}




	@Override
	public int getItemBarColor(ItemStack stack) {
		return HoneyableUtil.ITEM_BAR_COLOR;
	}
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {return Math.round(13.0F/(float) HoneyableUtil.MAX_HONEY_LEVEL * (float) honeyLevel);
	}



}
