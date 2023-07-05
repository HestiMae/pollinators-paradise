package garden.hestia.pollinators_paradise.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ChorusHoneyBlock extends HoneyBlock {
	public ChorusHoneyBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	protected void updateSlidingVelocity(Entity entity) {
		super.updateSlidingVelocity(entity);
		Vec3d vec3d = entity.getVelocity();
		if (vec3d.y < -0.13) {
			double d = -0.05 / vec3d.y;
			entity.setVelocity(new Vec3d(vec3d.x * d, -0.05, vec3d.z * d));
		} else {
			entity.setVelocity(new Vec3d(vec3d.x, 0.2, vec3d.z));
		}

		entity.resetFallDistance();
	}

	@Override
	protected boolean isSliding(BlockPos pos, Entity entity) {
		if (entity.isOnGround()) {
			return false;
		} else if (entity.getY() > (double) pos.getY() + 0.9375 - 1.0E-7) {
			return false;
		} else {
			double d = Math.abs((double) pos.getX() + 0.5 - entity.getX());
			double e = Math.abs((double) pos.getZ() + 0.5 - entity.getZ());
			double f = 0.4375 + (double) (entity.getWidth() / 2.0F);
			return d + 1.0E-7 > f || e + 1.0E-7 > f;
		}
	}
}
