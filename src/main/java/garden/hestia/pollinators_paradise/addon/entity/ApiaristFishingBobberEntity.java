package garden.hestia.pollinators_paradise.addon.entity;

import garden.hestia.pollinators_paradise.addon.PollinatorsEntities;
import garden.hestia.pollinators_paradise.addon.PollinatorsItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ApiaristFishingBobberEntity extends FishingBobberEntity {
	public ApiaristFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(thrower, world, luckOfTheSeaLevel, lureLevel);
		this.type = PollinatorsEntities.FISHING_BOBBER;
	}

	public ApiaristFishingBobberEntity(EntityType<? extends FishingBobberEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean removeIfInvalid(PlayerEntity player) {
		ItemStack itemStack = player.getMainHandStack();
		ItemStack itemStack2 = player.getOffHandStack();
		boolean bl = itemStack.isOf(PollinatorsItems.APIARIST_FISHING_ROD);
		boolean bl2 = itemStack2.isOf(PollinatorsItems.APIARIST_FISHING_ROD);
		if (!player.isRemoved() && player.isAlive() && (bl || bl2) && !(this.squaredDistanceTo(player) > 1024.0)) {
			return false;
		} else {
			this.discard();
			return true;
		}
	}
}
