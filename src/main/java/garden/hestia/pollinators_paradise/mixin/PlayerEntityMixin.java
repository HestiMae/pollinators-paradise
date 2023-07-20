package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.*;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PollinatorPlayerEntity {
	private static final double CALMING_BOX_SIZE = 10;
	private static final double ALLY_BOX_SIZE = 30;
	private static final double ATTACKER_BOX_SIZE = 16;
	@Unique
	private final WelliesJumpingMount welliesMount = new WelliesJumpingMount((PlayerEntity) (Object) this);
	@Unique
	private int pollenCharges;
	@Unique
	private int pollenCooldown;
	@Unique
	private int faithWalkingTicks;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Inject(method = "tick", at = @At(value = "TAIL"))
	public void veilTick(CallbackInfo ci) {
		ItemStack helmetStack = getEquippedStack(EquipmentSlot.HEAD);
		if (helmetStack.isOf(PollinatorsItems.APIARIST_VEIL) &&
				helmetStack.getItem() instanceof Honeyable honeyItem && helmetStack.getCooldown() == 0) {
			Vec3d pos = this.getPos();
			if (honeyItem.getHoneyType(helmetStack) == HoneyTypes.HONEY) {
				List<BeeEntity> calmableBees = this.getWorld().getNonSpectatingEntities(BeeEntity.class, Box.of(pos, CALMING_BOX_SIZE, CALMING_BOX_SIZE, CALMING_BOX_SIZE)).stream().filter(y -> y.getAngryAt() == this.uuid).toList();
				if (!calmableBees.isEmpty() && honeyItem.decrementHoneyLevel(helmetStack, HoneyTypes.HONEY)) {
					for (BeeEntity calmableBee : calmableBees) {
						calmableBee.stopAnger();
					}
					helmetStack.setCooldown(20);
				}
			} else if (honeyItem.getHoneyType(helmetStack) == HoneyTypes.CHORUS) {
				List<BeeEntity> allyBees = this.getWorld().getNonSpectatingEntities(BeeEntity.class, Box.of(pos, ALLY_BOX_SIZE, ALLY_BOX_SIZE, ALLY_BOX_SIZE)).stream().filter(b -> !b.hasAngerTime() && !b.hasStung()).toList();
				List<HostileEntity> attackers = this.getWorld().getNonSpectatingEntities(HostileEntity.class, Box.of(pos, ATTACKER_BOX_SIZE, ATTACKER_BOX_SIZE, ATTACKER_BOX_SIZE)).stream().filter(h -> h.isAngryAt((PlayerEntity) (Object) this)).toList();
				if (!allyBees.isEmpty() && !attackers.isEmpty()) {
					if (allyBees.stream().anyMatch(allyBee -> PollinatorsUtil.safeBeeAnger(allyBee, attackers.get(allyBee.getRandom().nextInt(attackers.size()))))) {
						honeyItem.decrementHoneyLevel(helmetStack, HoneyTypes.CHORUS);
						helmetStack.setCooldown(100);
					}
				}
			}
		}

	}

	@Override
	public WelliesJumpingMount getWelliesMount() {
		return welliesMount;
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	public void pantsTick(CallbackInfo ci) {
		ItemStack equippedLegStack = this.getEquippedStack(EquipmentSlot.LEGS);
		if (this.pollenCharges < 10 && equippedLegStack.isOf(PollinatorsItems.APIARIST_LEGGINGS)
				&& equippedLegStack.getItem() instanceof Honeyable honeyItem
				&& honeyItem.getHoneyType(equippedLegStack) == HoneyTypes.CHORUS) {
			for (int i = 0; i <= 1; ++i) {
				BlockPos blockPos = this.getBlockPos().down(i);
				BlockState blockState = this.getWorld().getBlockState(blockPos);
				if (blockState.isIn(BlockTags.FLOWERS)) {
					pollenCharges = 10;
					this.getWorld().syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
					honeyItem.decrementHoneyLevel(equippedLegStack, HoneyTypes.CHORUS);
				}
			}
		}

		if (equippedLegStack.isOf(PollinatorsItems.APIARIST_LEGGINGS) && this.pollenCharges > 0) {
			if (this.random.nextInt(30) == 0) {
				this.addParticle(
						this.getWorld(), this.getX() - 0.3F, this.getX() + 0.3F, this.getZ() - 0.3F, this.getZ() + 0.3F, this.getBodyY(0.5), ParticleTypes.FALLING_NECTAR
				);
			}
			if (pollenCooldown == 0) {
				if (this.random.nextInt(3) == 0) {
					this.addParticle(
							this.getWorld(), this.getX() - 0.3F, this.getX() + 0.3F, this.getZ() - 0.3F, this.getZ() + 0.3F, this.getBodyY(0.5), ParticleTypes.FALLING_NECTAR
					);
				}
				for (int i = 0; i <= 1; ++i) {
					BlockPos blockPos = this.getBlockPos().down(i);
					BlockState blockState = this.getWorld().getBlockState(blockPos);
					Block block = blockState.getBlock();
					BlockState blockState2 = null;
					if (blockState.isIn(BlockTags.BEE_GROWABLES)) {
						if (block instanceof CropBlock cropBlock) {
							if (!cropBlock.isMature(blockState)) {
								blockState2 = cropBlock.withAge(cropBlock.getAge(blockState) + 1);
							}
						} else if (block instanceof StemBlock) {
							int j = blockState.get(StemBlock.AGE);
							if (j < 7) {
								blockState2 = blockState.with(StemBlock.AGE, j + 1);
							}
						} else if (blockState.isOf(Blocks.SWEET_BERRY_BUSH)) {
							int j = blockState.get(SweetBerryBushBlock.AGE);
							if (j < 3) {
								blockState2 = blockState.with(SweetBerryBushBlock.AGE, j + 1);
							}
						} else if (blockState.isOf(Blocks.CAVE_VINES) || blockState.isOf(Blocks.CAVE_VINES_PLANT)) {
							((Fertilizable) blockState.getBlock()).fertilize((ServerWorld) this.getWorld(), this.random, blockPos, blockState);
						}

						if (blockState2 != null) {
							this.getWorld().syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
							this.getWorld().setBlockState(blockPos, blockState2);
							this.pollenCharges--;
							this.pollenCooldown = this.random.nextInt(220) + 40;
						}
					}
				}
			}
		}
		if (pollenCooldown > 0) pollenCooldown--;
	}

	private void addParticle(World world, double lastX, double x, double lastZ, double z, double y, ParticleEffect effect) {
		world.addParticle(effect, MathHelper.lerp(world.random.nextDouble(), lastX, x), y, MathHelper.lerp(world.random.nextDouble(), lastZ, z), 0.0, 0.0, 0.0);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void welliesTick(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity) (Object) this;
		ItemStack equippedFeetStack = self.getEquippedStack(EquipmentSlot.FEET);
		if (equippedFeetStack.isOf(PollinatorsItems.APIARIST_WELLIES) && equippedFeetStack.getItem() instanceof Honeyable honeyItem
				&& honeyItem.getHoneyType(equippedFeetStack) == HoneyTypes.CHORUS) {
			if (self.getPitch() > 60.0F || self.isOnGround() || faithWalkingTicks < 30) {
				faithWalkingTicks--;
			}
			if (self.isSprinting() && self.isOnGround() && faithWalkingTicks < 40) {
				faithWalkingTicks = 40;
			}

			if (self.isSprinting() && !self.isOnGround() && faithWalkingTicks > 0) {
				self.setVelocity(self.getVelocity().x, 0, self.getVelocity().z);
			}
			if (faithWalkingTicks == 0) honeyItem.decrementHoneyLevel(equippedFeetStack, HoneyTypes.CHORUS);
		} else {
			faithWalkingTicks = 0;
		}

	}

	@Inject(method = "jump", at = @At(value = "TAIL"))
	private void jumpDecrementFaithWalkingTicks(CallbackInfo ci) {
		faithWalkingTicks = 0;
	}

	@Override
	public int getFaithWalkingTicks() {
		return faithWalkingTicks;
	}
}

