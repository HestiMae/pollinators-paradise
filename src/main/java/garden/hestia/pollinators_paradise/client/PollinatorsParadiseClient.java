package garden.hestia.pollinators_paradise.client;

import garden.hestia.pollinators_paradise.HoneyTypes;
import garden.hestia.pollinators_paradise.PollinatorsEntities;
import garden.hestia.pollinators_paradise.PollinatorsItems;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class PollinatorsParadiseClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), PollinatorsItems.CHORUS_HONEY_BLOCK);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> tintIndex == 0 ? -1 : ((Honeyable) stack.getItem()).getItemTintColor(stack),
				PollinatorsItems.APIARIST_VEIL, PollinatorsItems.APIARIST_SUIT, PollinatorsItems.APIARIST_LEGGINGS, PollinatorsItems.APIARIST_WELLIES, PollinatorsItems.APIARIST_SHEARS
		);
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("honeyed"), ((itemStack, clientWorld, livingEntity, i) -> {
			if (itemStack.getItem() instanceof Honeyable honeyItem && honeyItem.getHoneyType(itemStack) != null) {
				return 1;
			}
			return 0;
		}));
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("armor_honeyed_honey"), ((itemStack, clientWorld, livingEntity, i) -> {
			int honey = 0;
			int chorus = 0;
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem) {
						if (honeyItem.getHoneyType(stack) == HoneyTypes.HONEY) {
							honey++;
						}
						if (honeyItem.getHoneyType(stack) == HoneyTypes.CHORUS) {
							chorus++;
						}
					}
				}
			}
			return honey > 0 && chorus == 0 ? 1 : 0;
		}));
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("armor_honeyed_mixed"), ((itemStack, clientWorld, livingEntity, i) -> {
			int honey = 0;
			int chorus = 0;
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem) {
						if (honeyItem.getHoneyType(stack) == HoneyTypes.HONEY) {
							honey++;
						}
						if (honeyItem.getHoneyType(stack) == HoneyTypes.CHORUS) {
							chorus++;
						}
					}
				}
			}
			return honey > 0 && chorus > 0 ? 1 : 0;
		}));
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("armor_honeyed_chorus"), ((itemStack, clientWorld, livingEntity, i) -> {
			int honey = 0;
			int chorus = 0;
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem) {
						if (honeyItem.getHoneyType(stack) == HoneyTypes.HONEY) {
							honey++;
						}
						if (honeyItem.getHoneyType(stack) == HoneyTypes.CHORUS) {
							chorus++;
						}
					}
				}
			}
			return honey == 0 && chorus > 0 ? 1 : 0;
		}));
		EntityRendererRegistry.register(PollinatorsEntities.FISHING_BOBBER, ApiaristFishingBobberEntityRenderer::new);
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("cast"), ((stack, world, entity, i) -> {
			if (entity == null) return 0;
			return (entity.getMainHandStack() == stack || entity.getOffHandStack() == stack) && entity instanceof PlayerEntity player && player.fishHook != null ? 1 : 0;
		}));
	}
}
