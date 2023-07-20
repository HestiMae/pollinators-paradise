package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.block.ChorusHoneyBlock;
import garden.hestia.pollinators_paradise.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static garden.hestia.pollinators_paradise.item.ApiaristArmorMaterial.APIARIST;

@SuppressWarnings("unused")
public class PollinatorsParadise implements ModInitializer {
	private static final String ID = "pollinators_paradise";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final Identifier C2S_WALLJUMP = PollinatorsParadise.id("walljump");
	public static final float SAFE_BEE_HEALTH = 4.0F;
	public static boolean FEED_THE_BEES_PRESENT = false;

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	public static boolean safeBeeAnger(BeeEntity bee, LivingEntity target) {
		if (bee.getHealth() > SAFE_BEE_HEALTH) {
			if (FEED_THE_BEES_PRESENT) {
				bee.setAttacker(target);
				bee.setAngryAt(target.getUuid());
				bee.chooseRandomAngerTime();
				return true;
			} else if (bee.hasStatusEffect(StatusEffects.REGENERATION)) {
				bee.setAttacker(target);
				bee.setAngryAt(target.getUuid());
				bee.setAngerTime(Math.min(bee.getStatusEffect(StatusEffects.REGENERATION).getDuration(), 780));
				return true;
			} else if (bee.hasStatusEffect(StatusEffects.RESISTANCE)) {
				bee.setAttacker(target);
				bee.setAngryAt(target.getUuid());
				bee.setAngerTime(Math.min(bee.getStatusEffect(StatusEffects.RESISTANCE).getDuration(), 780));
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void onInitialize(ModContainer mod) {
		ServerPlayNetworking.registerGlobalReceiver(C2S_WALLJUMP, ((server, player, handler, buf, responseSender) -> {
			if (player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof Honeyable honeyItem && honeyItem.decrementHoneyLevel(player.getEquippedStack(EquipmentSlot.LEGS), HoneyTypes.HONEY)) {
				player.fallDistance = 0;
			}
		}));

		if (QuiltLoader.isModLoaded("feed-the-bees")) {
			FEED_THE_BEES_PRESENT = true;
		}

		StatusEffects.SPEED.addAttributeModifier(EntityAttributes.GENERIC_FLYING_SPEED, "847abf1d-d98e-4cc8-9a8e-3d097b6c8268", 0.2F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		PollinatorsItems.initialize();
		HoneyTypes.initialize();

		LOGGER.info("[Pollinators' Paradise] Buzzing... Buzzed. Minecraft pollination successful");
	}
}
