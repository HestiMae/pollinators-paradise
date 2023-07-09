package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.block.ChorusHoneyBlock;
import garden.hestia.pollinators_paradise.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;
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
	public static final Block WAXED_WOOL_BLOCK = Registry.register(Registries.BLOCK, new Identifier(ID, "waxed_wool"), new Block(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final BlockItem WAXED_WOOL_ITEM = Registry.register(Registries.ITEM, new Identifier(ID, "waxed_wool"), new BlockItem(WAXED_WOOL_BLOCK, new QuiltItemSettings()));
	public static final Block CHORUS_HONEY_BLOCK = Registry.register(Registries.BLOCK, new Identifier(ID, "chorus_honey_block"), new ChorusHoneyBlock(AbstractBlock.Settings.create().mapColor(MapColor.PURPLE).velocityMultiplier(0.4F).jumpVelocityMultiplier(0.5F).nonOpaque().sounds(BlockSoundGroup.HONEY)));
	public static final BlockItem CHORUS_HONEY_BLOCK_ITEM = Registry.register(Registries.ITEM, new Identifier(ID, "chorus_honey_block"), new BlockItem(CHORUS_HONEY_BLOCK, new QuiltItemSettings()));
	public static final FoodComponent CHORUS_HONEY_BOTTLE_FOOD = new FoodComponent.Builder().hunger(6).saturationModifier(0.1F).alwaysEdible().build();
	public static final Item CHORUS_HONEY_BOTTLE = Registry.register(Registries.ITEM, new Identifier(ID, "chorus_honey_bottle"), new ChorusHoneyBottleItem(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).food(CHORUS_HONEY_BOTTLE_FOOD).maxCount(16)));

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
			entries.addAfter(Items.HONEYCOMB, WAXED_WOOL_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE_BLOCKS).register(entries -> {
			entries.addAfter(Items.HONEY_BLOCK, CHORUS_HONEY_BLOCK_ITEM);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINKS).register(entries -> {
			entries.addAfter(Items.HONEY_BOTTLE, CHORUS_HONEY_BOTTLE);
		});
		BrewingRecipeRegistry.ITEM_RECIPES.add(new BrewingRecipeRegistry.Recipe<>(Items.HONEY_BOTTLE, Ingredient.ofItems(Items.CHORUS_FRUIT), CHORUS_HONEY_BOTTLE));
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.register(context -> {
		});

		StatusEffects.SPEED.addAttributeModifier(EntityAttributes.GENERIC_FLYING_SPEED, "847abf1d-d98e-4cc8-9a8e-3d097b6c8268", 0.2F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		LOGGER.info("[Pollinators' Paradise] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
