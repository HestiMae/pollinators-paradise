package garden.hestia.pollinators_paradise.block;

import garden.hestia.pollinators_paradise.PollinatorLivingEntity;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.client.PollinatorsParadiseClient;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ChorusHoneyBlock extends HoneyBlock {
	public ChorusHoneyBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (this.isSliding(pos, entity)) {
			this.triggerAdvancement(entity, pos);
			if (!this.tryWallJump(entity, pos))
			{
				this.updateSlidingVelocity(entity);
			}
			this.addCollisionEffects(world, entity);
		}
	}

	private boolean tryWallJump(Entity entity, BlockPos pos) {
		if (entity instanceof PlayerEntity player) {
			ItemStack equippedLegStack = player.getEquippedStack(EquipmentSlot.LEGS);
			if (equippedLegStack.isOf(PollinatorsParadise.APIARIST_LEGGINGS) && equippedLegStack.getItem() instanceof Honeyable honeyItem) {
				if (player instanceof PollinatorLivingEntity pollinatorPlayer && pollinatorPlayer.pollinators$jumping()) {
					if (pollinatorPlayer.pollinators$jumpCooldown() <= 0 && honeyItem.getHoneyLevel(equippedLegStack, Honeyable.HoneyType.HONEY) > 0) {
						if (entity.getWorld().isClient()) {
							PollinatorsParadiseClient.walljump();
						}
						pollinatorPlayer.pollinators$wallJump(pos);
					}
					return true;
				}
			}
		}
		return false;
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

	@Override
	protected void addCollisionEffects(World world, Entity entity) {
		if (entity instanceof LivingEntity || entity instanceof AbstractMinecartEntity || entity instanceof TntEntity || entity instanceof BoatEntity) {
			if (world.random.nextInt(5) == 0) {
				entity.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
			}

			if (world.random.nextInt(5) == 0) {
				BlockState blockState = PollinatorsParadise.CHORUS_HONEY_BLOCK.getDefaultState();
				for(int i = 0; i < 10; ++i) {
					entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
				}
			}
		}
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
		BlockState blockState = PollinatorsParadise.CHORUS_HONEY_BLOCK.getDefaultState();

		for(int i = 0; i < 10; ++i) {
			entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
		}

		if (entity.handleFallDamage(fallDistance, 0.2F, world.getDamageSources().fall())) {
			entity.playSound(this.soundGroup.getFallSound(), this.soundGroup.getVolume() * 0.5F, this.soundGroup.getPitch() * 0.75F);
		}
	}
}
