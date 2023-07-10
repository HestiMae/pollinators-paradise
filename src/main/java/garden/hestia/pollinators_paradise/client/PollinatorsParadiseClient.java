package garden.hestia.pollinators_paradise.client;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class PollinatorsParadiseClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), PollinatorsParadise.CHORUS_HONEY_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((Honeyable)stack.getItem()).getItemBarColor(stack),
				PollinatorsParadise.APIARIST_VEIL, PollinatorsParadise.APIARIST_SUIT, PollinatorsParadise.APIARIST_LEGGINGS, PollinatorsParadise.APIARIST_WELLIES, PollinatorsParadise.APIARIST_SHEARS);
	}
}
