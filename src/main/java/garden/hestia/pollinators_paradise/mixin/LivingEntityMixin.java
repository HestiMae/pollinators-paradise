package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorLivingEntity;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements PollinatorLivingEntity{
	@Shadow
	protected boolean jumping;
	@Shadow
	private int jumpingCooldown;

	public LivingEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Shadow
	protected abstract void jump();

	@Shadow
	protected abstract float getJumpVelocity();

	@Shadow
	protected abstract void playBlockFallSound();

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
	@SuppressWarnings("ConstantConditions")
	@ModifyVariable(method = "travel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
	public float emptyWelliesSlippery(float original) {
		if ((Object) this instanceof PlayerEntity player)
		{
			ItemStack feetStack = player.getEquippedStack(EquipmentSlot.FEET);
			if (feetStack.isOf(PollinatorsParadise.APIARIST_WELLIES)
				&& feetStack.getItem() instanceof Honeyable honeyItem
					&& honeyItem.getHoneyType(feetStack) == Honeyable.HoneyType.NONE)
			{
				return 0.95F;
			}
		}


		return original;
	}

}
