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
	public static final UUID[] PROTECTION_MODIFIERS = new UUID[]{
			UUID.fromString("eae799c7-c5a3-4a31-bf39-246d78986490"),
			UUID.fromString("08cd2b92-1b07-48f0-8502-d60a0a08d189"),
			UUID.fromString("91d92410-9dc1-4cd1-b30c-8c04936fa931"),
			UUID.fromString("18d48019-acba-41bc-a8c6-5d1a48345e74"),
	};
	public static final UUID[] KNOCKBACK_MODIFIERS = new UUID[]{
			UUID.fromString("eae799c7-c5a3-4a31-bf39-246d78986490"),
			UUID.fromString("32df7056-4c58-418a-a7a2-59e9caf0fa33"),
			UUID.fromString("4f021b00-c863-4af0-b6bb-dd26bbb650f7"),
			UUID.fromString("0f87128d-6d61-4ebc-aeff-b2df8c39e134"),
	};
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
