package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.item.ApiaristArmorMaterial;
import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollinatorsParadise implements ModInitializer {
	public static final String ID = "pollinators_paradise";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final ApiaristArmorMaterial APIARIST_ARMOR_MATERIAL = new ApiaristArmorMaterial();
	public static final Item APIARIST_VEIL = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings());
	public static final Item APIARIST_WELLIES = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings());
	public static final Item APIARIST_SUIT = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings());
	public static final Item HONEYED_WOOL = new Item(new QuiltItemSettings());
	@Override
	public void onInitialize(ModContainer mod)
	{
		LOGGER.info("Mod initialising...");
		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_veil"), APIARIST_VEIL);
		LOGGER.info("Initialised apiarist veil");
		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_wellies"), APIARIST_WELLIES);
		LOGGER.info("Initialised apiarist wellies");
		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_suit"), APIARIST_SUIT);
		LOGGER.info("Initialised apiarist plate");
		Registry.register(Registries.ITEM, new Identifier(ID, "honeyed_wool"), HONEYED_WOOL);
		LOGGER.info("Initialised honeyed wool");

	}
}
