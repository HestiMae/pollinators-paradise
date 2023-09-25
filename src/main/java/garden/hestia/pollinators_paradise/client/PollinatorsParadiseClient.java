package garden.hestia.pollinators_paradise.client;

import garden.hestia.pollinators_paradise.*;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
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
		ColorProviderRegistry.ITEM.register(new HoneyColorProvider(), PollinatorsItems.APIARIST_WAND);
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("honeyed"), ((stack, clientWorld, livingEntity, i) -> ((Honeyable) stack.getItem()).getHoneyType(stack) != null ? 1 : 0));
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("equipment_honeyed"), ((stack, clientWorld, livingEntity, i) -> PollinatorsUtil.isStackInInventory(stack, livingEntity) && !Honeyable.getEquippedHoneyQuarters(livingEntity).isEmpty() ? 1 : 0));
	}
}
