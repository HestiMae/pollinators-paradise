package garden.hestia.pollinators_paradise.base.client;

import garden.hestia.pollinators_paradise.base.Honeyable;
import garden.hestia.pollinators_paradise.base.PollinatorsParadise;
import garden.hestia.pollinators_paradise.base.PollinatorsUtil;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class PollinatorsParadiseClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("honeyed"), ((stack, clientWorld, livingEntity, i) -> ((Honeyable) stack.getItem()).getHoneyType(stack) != null ? 1 : 0));
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("equipment_honeyed"), ((stack, clientWorld, livingEntity, i) -> PollinatorsUtil.isStackInInventory(stack, livingEntity) && !Honeyable.getEquippedHoneyQuarters(livingEntity).isEmpty() ? 1 : 0));
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(),
				Blocks.BEE_NEST
		);
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), Blocks.BEE_NEST);
	}
}
