package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
	public BlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "onSteppedOn", at = @At(value = "TAIL"))
	@SuppressWarnings("ConstantConditions")
	public void honeyOnSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
		if ((Object) this instanceof HoneyBlock) {
			if (entity instanceof PlayerEntity player) {
				ItemStack equippedFeetStack = player.getEquippedStack(EquipmentSlot.FEET);
				if (equippedFeetStack.isOf(PollinatorsParadise.APIARIST_WELLIES)
						&& equippedFeetStack.getItem() instanceof Honeyable honeyItem
						&& player.isSneaking()) {
					if (honeyItem.getHoneyType(equippedFeetStack) == Honeyable.HoneyType.HONEY)
					{
						player.setVelocity(Vec3d.ZERO);
					}
					if (!player.hasStatusEffect(StatusEffects.RESISTANCE)
							&& honeyItem.decrementHoneyLevel(equippedFeetStack, 2, Honeyable.HoneyType.HONEY)) {
						player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, true, true), null);
					}
				}
			}

		}
	}
}
