package garden.hestia.pollinators_paradise.client;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class PollinatorsParadiseClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), PollinatorsParadise.CHORUS_HONEY_BLOCK);
	}
}
