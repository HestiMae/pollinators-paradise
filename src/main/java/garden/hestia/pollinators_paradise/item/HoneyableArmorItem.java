package garden.hestia.pollinators_paradise.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HoneyableArmorItem extends ArmorItem implements Honeyable {
	private final int bottleCapacity;
	private final int bottlePoints;
	public static final UUID HONEY_PROTECTION_ID = UUID.fromString("eae799c7-c5a3-4a31-bf39-246d78986490");
	public static final UUID CHORUS_KNOCKBACK_ID = UUID.fromString("3754da21-304e-4a7a-a1cf-d054ede06d42");
	private final Map<HoneyType, String> tooltips;


	public HoneyableArmorItem(ArmorMaterial material, ArmorSlot slot, Settings settings, int bottleCapacity, int bottlePoints, Map<HoneyType, String> tooltips) {
		super(material, slot, settings);
		this.bottleCapacity = bottleCapacity;
		this.bottlePoints = bottlePoints;
		this.tooltips = tooltips;
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
	public int getItemBarStep(ItemStack stack) {
		return Honeyable.super.getItemBarStep(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		String honeyTooltip = tooltips.get(getHoneyType(stack));
		if (honeyTooltip != null)
		{
			tooltip.add(Text.literal(honeyTooltip).setStyle(Style.EMPTY.withColor(getItemBarColor(stack))));
		}
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
