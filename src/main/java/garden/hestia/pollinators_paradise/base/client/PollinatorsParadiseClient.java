package garden.hestia.pollinators_paradise.base.client;

import garden.hestia.pollinators_paradise.base.Honeyable;
import garden.hestia.pollinators_paradise.base.PollinatorsParadise;
import garden.hestia.pollinators_paradise.base.PollinatorsUtil;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class PollinatorsParadiseClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("honeyed"), ((stack, clientWorld, livingEntity, i) -> ((Honeyable) stack.getItem()).getHoneyType(stack) != null ? 1 : 0));
		ModelPredicateProviderRegistry.register(PollinatorsParadise.id("equipment_honeyed"), ((stack, clientWorld, livingEntity, i) -> PollinatorsUtil.isStackInInventory(stack, livingEntity) && !Honeyable.getEquippedHoneyQuarters(livingEntity).isEmpty() ? 1 : 0));
	}
}
