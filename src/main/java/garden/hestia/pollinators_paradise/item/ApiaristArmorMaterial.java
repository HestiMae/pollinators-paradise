package garden.hestia.pollinators_paradise.item;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class ApiaristArmorMaterial implements ArmorMaterial {
	@Override
	public int getDurability(ArmorItem.ArmorSlot slot) {
		return 0;
	}

	@Override
	public int getProtection(ArmorItem.ArmorSlot slot) {
		return 1;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.EMPTY;
	}

	@Override
	public String getName() {
		return "apiarist";
	}

	@Override
	public float getToughness() {
		return 0;
	}

	@Override
	public float getKnockbackResistance() {
		return 0;
	}

	@Override
	public @ClientOnly @NotNull Identifier getTexture() {
		return new Identifier(PollinatorsParadise.ID, "textures/models/armor/apiarist");
	}
}
