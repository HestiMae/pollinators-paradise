package garden.hestia.pollinators_paradise.addon.client;

import garden.hestia.pollinators_paradise.addon.PollinatorsEntities;
import garden.hestia.pollinators_paradise.addon.PollinatorsItems;
import garden.hestia.pollinators_paradise.base.HoneyColorProvider;
import garden.hestia.pollinators_paradise.base.Honeyable;
import garden.hestia.pollinators_paradise.base.PollinatorsParadise;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class PollinatorsAddonClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), PollinatorsItems.CHORUS_HONEY_BLOCK);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> tintIndex == 0 ? -1 : ((Honeyable) stack.getItem()).getItemTintColor(stack),
				PollinatorsItems.APIARIST_VEIL, PollinatorsItems.APIARIST_SUIT, PollinatorsItems.APIARIST_LEGGINGS, PollinatorsItems.APIARIST_WELLIES, PollinatorsItems.APIARIST_SHEARS
		);
		ColorProviderRegistry.ITEM.register(new HoneyColorProvider(), PollinatorsItems.APIARIST_WAND);
		EntityRendererRegistry.register(PollinatorsEntities.FISHING_BOBBER, ApiaristFishingBobberEntityRenderer::new);
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("cast"), ((stack, world, entity, i) -> {
			if (entity == null) return 0;
			return (entity.getMainHandStack() == stack || entity.getOffHandStack() == stack) && entity instanceof PlayerEntity player && player.fishHook != null ? 1 : 0;
		}));
	}
}
