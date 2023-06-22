package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.item.ApiaristArmorMaterial;
import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import garden.hestia.pollinators_paradise.item.HoneyableShearsItem;
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
	public static final Item APIARIST_VEIL = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings(), 4, 16);
	public static final Item APIARIST_WELLIES = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings(), 4, 512);
	public static final Item APIARIST_SUIT = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings(), 4, 64);
	public static final Item APIARIST_LEGGINGS = new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings(), 4, 128);
	public static final Item APIARIST_SHEARS = new HoneyableShearsItem(new QuiltItemSettings(), 4, 32);
	public static final Item APIARIST_WAND = new Item(new QuiltItemSettings());
	public static final Item HONEYED_WOOL = new Item(new QuiltItemSettings());
	@Override
	public void onInitialize(ModContainer mod)
	{
		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_veil"), APIARIST_VEIL);

		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_wellies"), APIARIST_WELLIES);

		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_suit"), APIARIST_SUIT);

		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_leggings"), APIARIST_LEGGINGS);

		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_shears"), APIARIST_SHEARS);

		Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_wand"), APIARIST_WAND);

		Registry.register(Registries.ITEM, new Identifier(ID, "honeyed_wool"), HONEYED_WOOL);

		LOGGER.info("[Pollinators' Paradise] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
