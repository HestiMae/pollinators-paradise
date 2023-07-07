package garden.hestia.pollinators_paradise.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerWorld;

public class HoneyWandItem extends Item {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public HoneyWandItem(Settings settings) {
		super(settings);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
				EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", -0.9F, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
				EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.20F, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
				EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.0F, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker instanceof PlayerEntity player) {
			int amount = 0;
			for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
				if (player.getEquippedStack(equipmentSlot).getItem() instanceof Honeyable honeyEquipment) {
					amount += honeyEquipment.getHoneyQuartile(player.getEquippedStack(equipmentSlot), Honeyable.HoneyType.HONEY);
					honeyEquipment.decrementHoneyLevel(player.getEquippedStack(equipmentSlot), Honeyable.HoneyType.HONEY);
				}
			}
			amount /= 2;
			if (amount > 0) {
				if (target instanceof BeeEntity) {
					target.heal(amount);
					if (target.getWorld() instanceof ServerWorld serverWorld) {
						serverWorld.getChunkManager().sendToNearbyPlayers(target, new EntityAnimationS2CPacket(target, EntityAnimationS2CPacket.ENCHANTED_HIT));
					}
				} else {
					target.damage(target.getWorld().getDamageSources().mobAttack(attacker), amount);
					if (target.getWorld() instanceof ServerWorld serverWorld) {
						serverWorld.getChunkManager().sendToNearbyPlayers(target, new EntityAnimationS2CPacket(target, EntityAnimationS2CPacket.CRIT));
					}
				}
			}

		}
		return super.postHit(stack, target, attacker);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
	}
}
