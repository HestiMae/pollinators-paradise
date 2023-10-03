package garden.hestia.pollinators_paradise.base;

import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollinatorsParadise implements ModInitializer {
	private static final String ID = "pollinators_paradise";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("[Pollinators' Base] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
