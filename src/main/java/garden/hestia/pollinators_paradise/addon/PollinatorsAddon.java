package garden.hestia.pollinators_paradise.addon;

import garden.hestia.pollinators_paradise.base.PollinatorsParadise;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

@SuppressWarnings("unused")
public class PollinatorsAddon implements ModInitializer {
	public static boolean FEED_THE_BEES_PRESENT = false;
	@Override
	public void onInitialize(ModContainer mod) {
		if (QuiltLoader.isModLoaded("feed-the-bees")) {
			FEED_THE_BEES_PRESENT = true;
		}
		StatusEffects.SPEED.addAttributeModifier(EntityAttributes.GENERIC_FLYING_SPEED, "847abf1d-d98e-4cc8-9a8e-3d097b6c8268", 0.2F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		PollinatorsNetworking.initialize();
		PollinatorsItems.initialize();
		PollinatorsEntities.initialize();
		HoneyTypes.initialize();
		PollinatorsParadise.LOGGER.info("[Pollinators' Addon] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
