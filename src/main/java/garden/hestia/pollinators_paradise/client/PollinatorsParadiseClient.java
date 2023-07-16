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
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class PollinatorsParadiseClient implements ClientModInitializer {
	public static void walljump() {
		ClientPlayNetworking.send(PollinatorsParadise.C2S_WALLJUMP, PacketByteBufs.empty());
	}

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
		ModelPredicateProviderRegistry.register(new Identifier(PollinatorsParadise.ID, "armor_honeyed_honey"), ((itemStack, clientWorld, livingEntity, i) -> {
			int honey = 0;
			int chorus = 0;
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem) {
						if (honeyItem.getHoneyType(stack) == Honeyable.HoneyType.HONEY) {
							honey++;
						}
						if (honeyItem.getHoneyType(stack) == Honeyable.HoneyType.CHORUS) {
							chorus++;
						}
					}
				}
			}
			return honey > 0 && chorus == 0 ? 1 : 0;
		}));
		ModelPredicateProviderRegistry.register(new Identifier(PollinatorsParadise.ID, "armor_honeyed_mixed"), ((itemStack, clientWorld, livingEntity, i) -> {
			int honey = 0;
			int chorus = 0;
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem) {
						if (honeyItem.getHoneyType(stack) == Honeyable.HoneyType.HONEY) {
							honey++;
						}
						if (honeyItem.getHoneyType(stack) == Honeyable.HoneyType.CHORUS) {
							chorus++;
						}
					}
				}
			}
			return honey > 0 && chorus > 0 ? 1 : 0;
		}));
		ModelPredicateProviderRegistry.register(new Identifier(PollinatorsParadise.ID, "armor_honeyed_chorus"), ((itemStack, clientWorld, livingEntity, i) -> {
			int honey = 0;
			int chorus = 0;
			if (livingEntity != null) {
				for (ItemStack stack : livingEntity.getItemsEquipped()) {
					if (stack.getItem() instanceof Honeyable honeyItem) {
						if (honeyItem.getHoneyType(stack) == Honeyable.HoneyType.HONEY) {
							honey++;
						}
						if (honeyItem.getHoneyType(stack) == Honeyable.HoneyType.CHORUS) {
							chorus++;
						}
					}
				}
			}
			return honey == 0 && chorus > 0 ? 1 : 0;
		}));
	}
}
