package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HoneyWandItem extends Item {
	public HoneyWandItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return super.postHit(stack, target, attacker);
	}
}
