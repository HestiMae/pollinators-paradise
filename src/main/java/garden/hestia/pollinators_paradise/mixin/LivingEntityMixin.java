package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

}
