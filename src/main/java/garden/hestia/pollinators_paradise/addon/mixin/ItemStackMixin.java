package garden.hestia.pollinators_paradise.addon.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import garden.hestia.pollinators_paradise.addon.HoneyTypes;
import garden.hestia.pollinators_paradise.addon.PollinatorsItems;
import garden.hestia.pollinators_paradise.base.PollinatorsUtil;
import garden.hestia.pollinators_paradise.addon.item.HoneyWandItem;
import garden.hestia.pollinators_paradise.base.HoneyableArmorItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static garden.hestia.pollinators_paradise.base.HoneyableArmorItem.KNOCKBACK_MODIFIERS;
import static garden.hestia.pollinators_paradise.base.HoneyableArmorItem.PROTECTION_MODIFIERS;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isOf(Item item);

	@Inject(at = @At("HEAD"), method = "isOf", cancellable = true)
	private void isApiaristShears(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item == Items.SHEARS) {
			if (isOf(PollinatorsItems.APIARIST_SHEARS)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(method = "getAttributeModifiers", at = @At("TAIL"), cancellable = true)
	private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.getItem() instanceof HoneyableArmorItem hai && hai.getArmorSlot().getEquipmentSlot() == slot) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
			//TODO: check for apiarist armour set
			builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(PROTECTION_MODIFIERS[slot.getEntitySlotId()], "honeyProtection", hai.getProtection() + (hai.getHoneyType(self) == HoneyTypes.HONEY ? 1 : 0), EntityAttributeModifier.Operation.ADDITION));
			if (hai.getHoneyType(self) == HoneyTypes.CHORUS) {
				builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(KNOCKBACK_MODIFIERS[slot.getEntitySlotId()], "chorusKnockback", hai.getMaterial().getKnockbackResistance() + 0.1, EntityAttributeModifier.Operation.ADDITION));
			}
			cir.setReturnValue(builder.build());
		}
	}

	@ModifyVariable(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 0), ordinal = 0)
	public List<Text> applyHoneyWandTooltip(List<Text> tooltip, @Nullable PlayerEntity player, TooltipContext context) {
		//TODO: refactor player context tooltips as interface
		ItemStack self = (ItemStack) (Object) this;
		if (self.getItem() instanceof HoneyWandItem hwi && PollinatorsUtil.isStackInInventory(self, player)) {
			hwi.appendTooltip(self, player, tooltip, context);
		}
		return tooltip;
	}
}
