package garden.hestia.pollinators_paradise.addon;

import net.minecraft.util.math.BlockPos;

public interface PollinatorLivingEntity {
	boolean pollinators$jumping();

	int pollinators$jumpCooldown();

	void pollinators$wallJump();

	void pollinators$wallJump(BlockPos pos);

}
