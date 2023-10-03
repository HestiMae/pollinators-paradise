package garden.hestia.pollinators_paradise.addon.mixin;

import com.google.common.collect.ImmutableSet;
import garden.hestia.pollinators_paradise.addon.PollinatorsItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {
	@ModifyVariable(at = @At("HEAD"), method = "<init>(Lnet/minecraft/registry/tag/TagKey;Ljava/util/Set;Lnet/minecraft/predicate/NumberRange$IntRange;Lnet/minecraft/predicate/NumberRange$IntRange;[Lnet/minecraft/predicate/item/EnchantmentPredicate;[Lnet/minecraft/predicate/item/EnchantmentPredicate;Lnet/minecraft/potion/Potion;Lnet/minecraft/predicate/NbtPredicate;)V", argsOnly = true)
	private static Set<Item> addApiaristShears(Set<Item> set) {
		if (set != null && set.contains(Items.SHEARS)) {
			set = new HashSet<>(set);
			set.add(PollinatorsItems.APIARIST_SHEARS);
			set = ImmutableSet.copyOf(set);
		}
		return set;
	}
}
