package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.HoneyableUtil;
import net.minecraft.block.Block;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow
	public World world;
	@Shadow
	protected abstract BlockPos getVelocityAffectingPos();
	@ModifyVariable(method = "getJumpVelocityMultiplier", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/Block;getJumpVelocityMultiplier()F"), index = 1)
	protected float applyHoneyBounce(float original)
	{
		Block block = this.world.getBlockState(this.getVelocityAffectingPos()).getBlock();
		if (block instanceof HoneyBlock)
		{
			if ((Object) this instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.FEET).isOf(PollinatorsParadise.APIARIST_WELLIES)
					&& HoneyableUtil.getHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET)) > 0 && !player.isSneaking())
			{
				HoneyableUtil.putHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET), HoneyableUtil.getHoneyLevel(player.getEquippedStack(EquipmentSlot.FEET)) - 32);
				return 1.8F;
			}
		}
		return original;
	}
}
