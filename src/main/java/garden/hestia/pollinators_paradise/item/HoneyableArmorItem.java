package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class HoneyableArmorItem extends ArmorItem {


	public HoneyableArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		return HoneyableUtil.onClicked(thisStack, otherStack, thisSlot, clickType, player);
	}
	@Override
	public int getItemBarColor(ItemStack stack) {
		return HoneyableUtil.getItemBarColor(stack);
	}
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return HoneyableUtil.isItemBarVisible(stack);
	}

	@Override
	public int getItemBarStep(ItemStack stack) {return HoneyableUtil.getItemBarStep(stack);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
}
