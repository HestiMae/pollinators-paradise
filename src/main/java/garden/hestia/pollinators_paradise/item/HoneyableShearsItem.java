package garden.hestia.pollinators_paradise.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HoneyableShearsItem extends ShearsItem implements Honeyable {
	private final int bottleCapacity;
	private final int bottlePoints;
	private final boolean noneTooltip;

	public HoneyableShearsItem(Settings settings, int bottleCapacity, int bottlePoints, boolean noneTooltip) {
		super(settings);
		this.bottleCapacity = bottleCapacity;
		this.bottlePoints = bottlePoints;
		this.noneTooltip = noneTooltip;
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		return Honeyable.super.onClicked(thisStack, otherStack, thisSlot, clickType, player, cursorStackReference);
	}

	@Override
	public boolean isDamageable() {
		return false;
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
	public int getItemBarStep(ItemStack stack) {
		return Honeyable.super.getItemBarStep(stack);
	}

	@Override
	public int bottleCapacity() {
		return bottleCapacity;
	}

	@Override
	public int bottlePoints() {
		return bottlePoints;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		Honeyable.super.appendTooltip(stack, tooltip, noneTooltip);
		tooltip.add(Text.translatable("tooltip.pollinators_paradise.apiarist_shears.gentle").setStyle(Style.EMPTY.withColor(0xdd7e68)));
	}
}
