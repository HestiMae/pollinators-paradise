package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.block.ChorusHoneyBlock;
import garden.hestia.pollinators_paradise.item.ChorusHoneyBottleItem;
import garden.hestia.pollinators_paradise.item.HoneyWandItem;
import garden.hestia.pollinators_paradise.item.HoneyableArmorItem;
import garden.hestia.pollinators_paradise.item.HoneyableFishingRodItem;
import garden.hestia.pollinators_paradise.item.HoneyableShearsItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.Map;

import static garden.hestia.pollinators_paradise.item.ApiaristArmorMaterial.APIARIST;

public class PollinatorsItems {
	public static final FoodComponent CHORUS_HONEY_BOTTLE_FOOD = new FoodComponent.Builder().hunger(6).saturationModifier(0.1F).alwaysEdible().build();

	public static final Block WAXED_WOOL_BLOCK = Registry.register(Registries.BLOCK, PollinatorsParadise.id("waxed_wool"), new Block(QuiltBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final Block CHORUS_HONEY_BLOCK = Registry.register(Registries.BLOCK, PollinatorsParadise.id("chorus_honey_block"), new ChorusHoneyBlock(AbstractBlock.Settings.create().mapColor(MapColor.PURPLE).velocityMultiplier(0.4F).jumpVelocityMultiplier(0.5F).nonOpaque().sounds(BlockSoundGroup.HONEY)));

	public static final BlockItem WAXED_WOOL_ITEM = Registry.register(Registries.ITEM, PollinatorsParadise.id("waxed_wool"), new BlockItem(WAXED_WOOL_BLOCK, new QuiltItemSettings()));
	public static final BlockItem CHORUS_HONEY_BLOCK_ITEM = Registry.register(Registries.ITEM, PollinatorsParadise.id("chorus_honey_block"), new BlockItem(CHORUS_HONEY_BLOCK, new QuiltItemSettings()));

	public static final Item CHORUS_HONEY_BOTTLE = Registry.register(Registries.ITEM, PollinatorsParadise.id("chorus_honey_bottle"), new ChorusHoneyBottleItem(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).food(CHORUS_HONEY_BOTTLE_FOOD).maxCount(16)));

	public static final Item APIARIST_VEIL = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_veil"), new HoneyableArmorItem(APIARIST, ArmorItem.ArmorSlot.HELMET, new QuiltItemSettings().maxCount(1).maxDamage(0), 4, 16, Map.of(HoneyTypes.HONEY, Text.translatable("tooltip.pollinators_paradise.apiarist_veil.honey"), HoneyTypes.CHORUS, Text.translatable("tooltip.pollinators_paradise.apiarist_veil.chorus"))));
	public static final Item APIARIST_WELLIES = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_wellies"), new HoneyableArmorItem(APIARIST, ArmorItem.ArmorSlot.BOOTS, new QuiltItemSettings().maxCount(1).maxDamage(0), 4, 512, Map.of(HoneyTypes.HONEY, Text.translatable("tooltip.pollinators_paradise.apiarist_wellies.honey"), HoneyTypes.CHORUS, Text.translatable("tooltip.pollinators_paradise.apiarist_wellies.chorus")), Text.translatable("tooltip.pollinators_paradise.apiarist_wellies.none")));
	public static final Item APIARIST_SUIT = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_suit"), new HoneyableArmorItem(APIARIST, ArmorItem.ArmorSlot.CHESTPLATE, new QuiltItemSettings().maxCount(1).maxDamage(0), 4, 64, Map.of(HoneyTypes.HONEY, Text.translatable("tooltip.pollinators_paradise.apiarist_suit.honey"), HoneyTypes.CHORUS, Text.translatable("tooltip.pollinators_paradise.apiarist_suit.chorus"))));
	public static final Item APIARIST_LEGGINGS = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_leggings"), new HoneyableArmorItem(APIARIST, ArmorItem.ArmorSlot.LEGGINGS, new QuiltItemSettings().maxCount(1).maxDamage(0), 4, 128, Map.of(HoneyTypes.HONEY, Text.translatable("tooltip.pollinators_paradise.apiarist_leggings.honey"), HoneyTypes.CHORUS, Text.translatable("tooltip.pollinators_paradise.apiarist_leggings.chorus"))));
	public static final Item APIARIST_SHEARS = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_shears"), new HoneyableShearsItem(new QuiltItemSettings().maxCount(1).maxDamage(0), 4, 32, Map.of(HoneyTypes.HONEY, Text.translatable("tooltip.pollinators_paradise.apiarist_shears.honey"), HoneyTypes.CHORUS, Text.translatable("tooltip.pollinators_paradise.apiarist_shears.chorus"))));
	public static final Item APIARIST_WAND = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_wand"), new HoneyWandItem(new QuiltItemSettings().maxCount(1).maxDamage(0), Map.of(HoneyTypes.HONEY, Text.translatable("tooltip.pollinators_paradise.apiarist_wand.honey"), HoneyTypes.CHORUS, Text.translatable("tooltip.pollinators_paradise.apiarist_wand.chorus"))));
	public static final Item APIARIST_FISHING_ROD = Registry.register(Registries.ITEM, PollinatorsParadise.id("apiarist_fishing_rod"), new HoneyableFishingRodItem(new QuiltItemSettings().maxDamage(64)));

	@SuppressWarnings("UnstableApiUsage")
	public static void initialize()
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
	}
}
