package garden.hestia.pollinators_paradise;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class PollinatorsParadise implements ModInitializer {
	private static final String ID = "pollinators_paradise";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static boolean FEED_THE_BEES_PRESENT = false;

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}



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

		LOGGER.info("[Pollinators' Paradise] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
