package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.item.ApiaristArmorMaterial;
import garden.hestia.pollinators_paradise.item.HoneyWandItem;
import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import garden.hestia.pollinators_paradise.item.HoneyableShearsItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class PollinatorsParadise implements ModInitializer {
	public static final String ID = "pollinators_paradise";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final ApiaristArmorMaterial APIARIST_ARMOR_MATERIAL = new ApiaristArmorMaterial();
	public static final Item APIARIST_VEIL = Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_veil"), new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings(), 4, 16));
	public static final Item APIARIST_WELLIES = Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_wellies"), new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings(), 4, 512));
	public static final Item APIARIST_SUIT = Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_suit"), new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings(), 4, 64));
	public static final Item APIARIST_LEGGINGS = Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_leggings"), new HoneyableArmorItem(APIARIST_ARMOR_MATERIAL, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings(), 4, 128));
	public static final Item APIARIST_SHEARS = Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_shears"), new HoneyableShearsItem(new QuiltItemSettings(), 4, 32));
	public static final Item APIARIST_WAND = Registry.register(Registries.ITEM, new Identifier(ID, "apiarist_wand"), new HoneyWandItem(new QuiltItemSettings()));
	public static final Block HONEYED_WOOL_BLOCK = Registry.register(Registries.BLOCK, new Identifier(ID, "honeyed_wool"), new Block(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final BlockItem HONEYED_WOOL_ITEM = Registry.register(Registries.ITEM, new Identifier(ID, "honeyed_wool"), new BlockItem(HONEYED_WOOL_BLOCK, new QuiltItemSettings()));
	@Override
	public void onInitialize(ModContainer mod)
	{
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.addBefore(Items.LEATHER_HELMET, APIARIST_VEIL, APIARIST_SUIT, APIARIST_LEGGINGS, APIARIST_WELLIES);
			entries.addAfter(Items.TRIDENT, APIARIST_WAND);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS_AND_UTILITIES).register(entries -> {
			entries.addAfter(Items.SHEARS, APIARIST_SHEARS);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
			entries.addAfter(Items.HONEYCOMB, HONEYED_WOOL_ITEM);
		});
		StatusEffects.SPEED.addAttributeModifier(EntityAttributes.GENERIC_FLYING_SPEED, "847abf1d-d98e-4cc8-9a8e-3d097b6c8268", 0.2F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		LOGGER.info("[Pollinators' Paradise] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
