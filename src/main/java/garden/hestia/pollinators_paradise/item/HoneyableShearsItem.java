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
import java.util.Map;

public class HoneyableShearsItem extends ShearsItem implements Honeyable {
	private final int bottleCapacity;
	private final int bottlePoints;
	private final Map<HoneyType, String> tooltips;

	public HoneyableShearsItem(Settings settings, int bottleCapacity, int bottlePoints, Map<HoneyType, String> tooltips) {
		super(settings);
		this.bottleCapacity = bottleCapacity;
		this.bottlePoints = bottlePoints;
		this.tooltips = tooltips;
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
		String honeyTooltip = tooltips.get(getHoneyType(stack));
		if (honeyTooltip != null) {
			tooltip.add(Text.literal(honeyTooltip).setStyle(Style.EMPTY.withColor(getItemBarColor(stack))));
		}
		tooltip.add(Text.literal("Gentle").setStyle(Style.EMPTY.withColor(0xdd7e68)));
	}
}
