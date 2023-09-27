package garden.hestia.pollinators_paradise;

import garden.hestia.pollinators_paradise.entity.ApiaristFishingBobberEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

public class PollinatorsEntities {
	public static final EntityType<ApiaristFishingBobberEntity> FISHING_BOBBER = Registry.register(
			Registries.ENTITY_TYPE,
			PollinatorsParadise.id("apiarist_fishing_bobber"),
			QuiltEntityTypeBuilder.<ApiaristFishingBobberEntity>create(SpawnGroup.MISC, ApiaristFishingBobberEntity::new)
					.disableSaving()
					.disableSummon()
					.setDimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.maxChunkTrackingRange(4)
					.trackingTickInterval(5)
					.build()
	);

	public static void initialize() {
	}
}
