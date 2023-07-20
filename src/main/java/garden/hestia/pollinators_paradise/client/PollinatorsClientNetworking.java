package garden.hestia.pollinators_paradise.client;

import garden.hestia.pollinators_paradise.PollinatorsNetworking;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class PollinatorsClientNetworking {
	public static void wallJump() {
		ClientPlayNetworking.send(PollinatorsNetworking.C2S_WALL_JUMP, PacketByteBufs.empty());
	}
}
