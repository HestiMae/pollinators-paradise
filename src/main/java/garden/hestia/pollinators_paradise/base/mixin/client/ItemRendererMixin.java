package garden.hestia.pollinators_paradise.base.mixin.client;

import garden.hestia.pollinators_paradise.base.HoneyColorProvider;
import garden.hestia.pollinators_paradise.base.HoneyColourable;
import garden.hestia.pollinators_paradise.base.Honeyable;
import garden.hestia.pollinators_paradise.base.PollinatorsUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At("HEAD"))
	public void applyHoldingEntityQuarters(LivingEntity entity, ItemStack stack, ModelTransformationMode modelTransformationMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
		if (stack.getItem() instanceof HoneyColourable && entity != null) {
			HoneyColorProvider.honeyQuarters = Honeyable.getEquippedHoneyQuarters(entity);
		}
	}

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
	public void applyClientPlayerQuarters(ItemStack stack, ModelTransformationMode modelTransformationMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		if (stack.getItem() instanceof HoneyColourable && HoneyColorProvider.honeyQuarters == null && PollinatorsUtil.isStackInInventory(stack, MinecraftClient.getInstance().player)) {
			HoneyColorProvider.honeyQuarters = Honeyable.getEquippedHoneyQuarters(MinecraftClient.getInstance().player);
		}
	}

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("TAIL"))
	public void renderItemTail(ItemStack stack, ModelTransformationMode modelTransformationMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		if (stack.getItem() instanceof HoneyColourable) {
			HoneyColorProvider.honeyQuarters = null;
		}
	}

}
