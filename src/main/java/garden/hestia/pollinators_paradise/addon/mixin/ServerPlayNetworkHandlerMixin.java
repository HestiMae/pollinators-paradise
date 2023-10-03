package garden.hestia.pollinators_paradise.addon.mixin;

import garden.hestia.pollinators_paradise.addon.PollinatorEntity;
import garden.hestia.pollinators_paradise.addon.PollinatorPlayerEntity;
import garden.hestia.pollinators_paradise.addon.block.ChorusHoneyBlock;
import net.minecraft.entity.JumpingMount;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "onClientCommand", at = @At(value = "TAIL"))
	void useWelliesMount(ClientCommandC2SPacket packet, CallbackInfo ci) {
		if (packet.getMode() == ClientCommandC2SPacket.Mode.START_RIDING_JUMP && player.getControlledVehicle() == null) {
			if (player instanceof PollinatorPlayerEntity pollinatorPlayer && player instanceof PollinatorEntity pollinatorEntity && pollinatorEntity.getCrystallised() instanceof ChorusHoneyBlock) {
				JumpingMount jumpingMount = pollinatorPlayer.getWelliesMount();
				int i = packet.getMountJumpHeight();
				if (jumpingMount.canJump() && i > 0) {
					jumpingMount.startJumping(i);
				}
			}
		}
	}
}
