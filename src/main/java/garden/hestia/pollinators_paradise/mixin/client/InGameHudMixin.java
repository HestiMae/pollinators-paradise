package garden.hestia.pollinators_paradise.mixin.client;

import garden.hestia.pollinators_paradise.PollinatorEntity;
import garden.hestia.pollinators_paradise.PollinatorPlayerEntity;
import garden.hestia.pollinators_paradise.block.ChorusHoneyBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.JumpingMount;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getJumpingMount()Lnet/minecraft/entity/JumpingMount;"))
	JumpingMount useWelliesMount(JumpingMount original) {
		ClientPlayerEntity player = client.player;
		if (player instanceof PollinatorPlayerEntity pollinatorSelf && player instanceof PollinatorEntity pollinatorEntity && pollinatorEntity.getCrystallised() instanceof ChorusHoneyBlock) {
			return pollinatorSelf.getWelliesMount();
		}
		return original;
	}
}
