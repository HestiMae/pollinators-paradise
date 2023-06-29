package garden.hestia.pollinators_paradise.item;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerWorld;

public class HoneyWandItem extends Item {
	public HoneyWandItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		PollinatorsParadise.LOGGER.info("postHit run");
		if (attacker instanceof PlayerEntity player) {
			int amount = 0;
			for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
				if (player.getEquippedStack(equipmentSlot).getItem() instanceof Honeyable honeyEquipment) {
					amount += honeyEquipment.getHoneyQuartile(player.getEquippedStack(equipmentSlot));
					honeyEquipment.decrementHoneyLevel(player.getEquippedStack(equipmentSlot), 1);
				}
			}
			amount /= 2;
			PollinatorsParadise.LOGGER.info("amount value: {}", amount);
			if (amount > 0) {
				target.damage(target.getWorld().getDamageSources().mobAttack(attacker), amount);
				if (target.getWorld() instanceof ServerWorld serverWorld) {
					serverWorld.getChunkManager().sendToNearbyPlayers(target, new EntityAnimationS2CPacket(attacker, EntityAnimationS2CPacket.CRIT));
				}
			}

		}
		return super.postHit(stack, target, attacker);
	}
}
