package garden.hestia.pollinators_paradise.addon.mixin;

import garden.hestia.pollinators_paradise.addon.PollinatorLivingEntity;
import garden.hestia.pollinators_paradise.addon.PollinatorPlayerEntity;
import garden.hestia.pollinators_paradise.addon.PollinatorsItems;
import garden.hestia.pollinators_paradise.base.Honeyable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements PollinatorLivingEntity {
	@Shadow
	protected boolean jumping;
	@Shadow
	private int jumpingCooldown;

	public LivingEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Shadow
	protected abstract float getJumpVelocity();

	@Shadow
	protected abstract void jump();

	@Override
	public boolean pollinators$jumping() {
		return jumping;
	}

	@Override
	public int pollinators$jumpCooldown() {
		return jumpingCooldown;
	}

	@Override
	public void pollinators$wallJump() {
		Vec3d vec3d = this.getVelocity();
		this.setVelocity(vec3d.x, this.getJumpVelocity() * 2, vec3d.z);
		this.velocityDirty = true;
		this.jumpingCooldown = 10;
	}

	@Override
	public void pollinators$wallJump(BlockPos pos) {
		float x = 0;
		float z = 0;
		if (this.getX() - pos.getX() > 0.9) {
			x = this.getJumpVelocity() * 2;
		} else if (this.getX() - pos.getX() < 0.1) {
			x = this.getJumpVelocity() * -2;
		}
		if (this.getZ() - pos.getZ() > 0.9) {
			z = this.getJumpVelocity() * 2;
		} else if (this.getZ() - pos.getZ() < 0.1) {
			z = this.getJumpVelocity() * -2;
		}
		Vec3d vec3d = this.getVelocity();
		this.setVelocity(x == 0 ? vec3d.x : x, this.getJumpVelocity() * 2, z == 0 ? vec3d.z : z);
		this.velocityDirty = true;
		this.jumpingCooldown = 10;
	}

	@SuppressWarnings("ConstantConditions")
	@ModifyVariable(method = "travel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
	public float emptyWelliesSlippery(float original) {
		if ((Object) this instanceof PlayerEntity player) {
			ItemStack feetStack = player.getEquippedStack(EquipmentSlot.FEET);
			if (feetStack.isOf(PollinatorsItems.APIARIST_WELLIES)
					&& feetStack.getItem() instanceof Honeyable honeyItem
					&& honeyItem.getHoneyType(feetStack) == null) {
				return 0.95F;
			}
		}


		return original;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void welliesTick(CallbackInfo ci) {
		if ((Object) this instanceof PollinatorPlayerEntity player && player.getFaithWalkingTicks() > 0 && this.jumpingCooldown == 0 && this.jumping)
			this.jump();
	}


}
