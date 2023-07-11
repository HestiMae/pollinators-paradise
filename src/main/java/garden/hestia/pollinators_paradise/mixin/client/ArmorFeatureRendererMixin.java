package garden.hestia.pollinators_paradise.mixin.client;

import garden.hestia.pollinators_paradise.item.Honeyable;
import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author unascribed
 */
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {
	@Shadow
	protected abstract void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, BipedEntityModel<?> model, boolean usesSecondLayer, float red, float green, float blue, @Nullable String overlay);


	@Inject(method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V", at = @At("TAIL"))
	private void apiaristThingy$renderArmor(MatrixStack matrices, VertexConsumerProvider vcp, LivingEntity entity, EquipmentSlot armorSlot, int light, BipedEntityModel<?> model, CallbackInfo ci) {
		var is = entity.getEquippedStack(armorSlot);
		if (is.getItem() instanceof HoneyableArmorItem hai && hai.getArmorSlot().getEquipmentSlot() == armorSlot && hai.getHoneyType(is) != Honeyable.HoneyType.NONE) {
			float[] colorComponents = hai.getArmorColor(is);
			renderArmorParts(matrices, vcp, light, hai, model, false, colorComponents[0], colorComponents[1], colorComponents[2], "overlay");
			renderArmorParts(matrices, vcp, light, hai, model, true, colorComponents[0], colorComponents[1], colorComponents[2], "overlay");
		}
	}
}
