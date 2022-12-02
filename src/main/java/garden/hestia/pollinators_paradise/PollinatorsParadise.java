package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class PollinatorsParadise implements ModInitializer {
	public static final String ID = "pollinators_paradise";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final Item APIARIST_VEIL = new HoneyableArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.HEAD,new QuiltItemSettings().group(ItemGroup.COMBAT));
	public static final Item HONEYED_WOOL = new Item(new QuiltItemSettings().group(ItemGroup.MATERIALS));
	@Override
	public void onInitialize(ModContainer mod)
	{
		LOGGER.info("Mod initialising...");
		Registry.register(Registry.ITEM, new Identifier(ID, "apiarist_veil"), APIARIST_VEIL);
		LOGGER.info("Initialised apiarist veil");
		Registry.register(Registry.ITEM, new Identifier(ID, "honeyed_wool"), HONEYED_WOOL);
		LOGGER.info("Initialised honeyed wool");

	}
}
