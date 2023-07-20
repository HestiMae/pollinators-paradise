package garden.hestia.pollinators_paradise;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;

public abstract class PollinatorsUtil {
	public static final float SAFE_BEE_HEALTH = 4.0F;
	public static boolean safeBeeAnger(BeeEntity bee, LivingEntity target) {
		if (bee.getHealth() > SAFE_BEE_HEALTH) {
			if (PollinatorsParadise.FEED_THE_BEES_PRESENT) {
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
}
