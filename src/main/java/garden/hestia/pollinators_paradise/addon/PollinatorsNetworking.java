package garden.hestia.pollinators_paradise.addon;

import garden.hestia.pollinators_paradise.base.Honeyable;
import garden.hestia.pollinators_paradise.base.PollinatorsParadise;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class PollinatorsNetworking {
	public static final Identifier C2S_WALL_JUMP = PollinatorsParadise.id("wall_jump");

	public static void initialize()
	{
		ServerPlayNetworking.registerGlobalReceiver(C2S_WALL_JUMP, ((server, player, handler, buf, responseSender) -> {
			if (player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof Honeyable honeyItem && honeyItem.decrementHoneyLevel(player.getEquippedStack(EquipmentSlot.LEGS), HoneyTypes.HONEY)) {
				player.fallDistance = 0;
			}
		}));
	}
}
