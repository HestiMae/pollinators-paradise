package garden.hestia.pollinators_paradise.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import garden.hestia.pollinators_paradise.HoneyTypes;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static garden.hestia.pollinators_paradise.item.HoneyableArmorItem.KNOCKBACK_MODIFIERS;
import static garden.hestia.pollinators_paradise.item.HoneyableArmorItem.PROTECTION_MODIFIERS;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean isOf(Item item);

	@Inject(at = @At("HEAD"), method = "isOf", cancellable = true)
	private void isApiaristShears(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item == Items.SHEARS) {
			if (isOf(PollinatorsParadise.APIARIST_SHEARS)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(method = "getAttributeModifiers", at = @At("TAIL"), cancellable = true)
	private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.getItem() instanceof HoneyableArmorItem hai && hai.getArmorSlot().getEquipmentSlot() == slot) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(PROTECTION_MODIFIERS[slot.getEntitySlotId()], "honeyProtection", hai.getProtection() + (hai.getHoneyType(self) == HoneyTypes.HONEY ? 1 : 0), EntityAttributeModifier.Operation.ADDITION));
			if (hai.getHoneyType(self) == HoneyTypes.CHORUS) {
				builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(KNOCKBACK_MODIFIERS[slot.getEntitySlotId()], "chorusKnockback", hai.getMaterial().getKnockbackResistance() + 0.1, EntityAttributeModifier.Operation.ADDITION));
			}
			cir.setReturnValue(builder.build());
		}
	}

}
