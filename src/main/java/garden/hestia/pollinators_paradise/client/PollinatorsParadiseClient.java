package garden.hestia.pollinators_paradise.client;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class PollinatorsParadiseClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), PollinatorsParadise.CHORUS_HONEY_BLOCK);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> tintIndex == 0 ? -1 : ((Honeyable) stack.getItem()).getItemTintColor(stack),
				PollinatorsParadise.APIARIST_VEIL, PollinatorsParadise.APIARIST_SUIT, PollinatorsParadise.APIARIST_LEGGINGS, PollinatorsParadise.APIARIST_WELLIES, PollinatorsParadise.APIARIST_SHEARS
		);
		ModelPredicateProviderRegistry.register(new Identifier(PollinatorsParadise.ID, "honeyed"), ((itemStack, clientWorld, livingEntity, i) -> {
			if (itemStack.getItem() instanceof Honeyable honeyItem && honeyItem.getHoneyType(itemStack) != Honeyable.HoneyType.NONE) {
				return 1;
			}
			return 0;
		}));
		ModelPredicateProviderRegistry.register(new Identifier(PollinatorsParadise.ID, "armor_honeyed"), ((itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem && honeyItem.getHoneyType(stack) != Honeyable.HoneyType.NONE) {
						return 1;
					}
				}
			}
			return 0;
		}));
	}
}
