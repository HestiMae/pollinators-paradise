package garden.hestia.pollinators_paradise.addon.mixin.client;

import garden.hestia.pollinators_paradise.addon.PollinatorEntity;
import garden.hestia.pollinators_paradise.addon.PollinatorPlayerEntity;
import garden.hestia.pollinators_paradise.addon.block.ChorusHoneyBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.JumpingMount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

	@ModifyVariable(method = "tickMovement", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getJumpingMount()Lnet/minecraft/entity/JumpingMount;"))
	JumpingMount useWelliesMount(JumpingMount original) {
		ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
		if (self instanceof PollinatorPlayerEntity pollinatorSelf && self instanceof PollinatorEntity pollinatorEntity && pollinatorEntity.getCrystallised() instanceof ChorusHoneyBlock) {
			return pollinatorSelf.getWelliesMount();
		}
		return original;
	}
}
