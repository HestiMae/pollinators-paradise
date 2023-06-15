package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.HoneyableUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	private static final double BOX_SIZE = 3;

	@Inject(method = "tick", at = @At(value = "TAIL"))
	public void tick(CallbackInfo ci)
	{
		if (this.getEquippedStack(EquipmentSlot.HEAD).isOf(PollinatorsParadise.APIARIST_VEIL) && HoneyableUtil.getHoneyLevel(getEquippedStack(EquipmentSlot.HEAD)) > 0)
		{
			Vec3d pos = this.getPos();
			List<Entity> localEntities = this.getWorld().getOtherEntities(this, new Box(pos.x - BOX_SIZE, pos.y - BOX_SIZE, pos.z - BOX_SIZE, pos.x + BOX_SIZE, pos.y + BOX_SIZE, pos.z + BOX_SIZE));
			localEntities.stream().filter(x -> x instanceof BeeEntity).filter(y -> ((BeeEntity) y).getAngryAt() == this.uuid).forEach(y -> {
				((BeeEntity) y).stopAnger();
				HoneyableUtil.putHoneyLevel(getEquippedStack(EquipmentSlot.HEAD), HoneyableUtil.getHoneyLevel(getEquippedStack(EquipmentSlot.HEAD)) - 64);
			});

		}

	}
}
