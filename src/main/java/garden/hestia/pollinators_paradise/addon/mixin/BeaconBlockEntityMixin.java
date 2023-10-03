package garden.hestia.pollinators_paradise.addon.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HoneyBlock;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {

	@Unique
	private static Block stainedBlock = null;

	@ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/DyeColor;getColorComponents()[F"), ordinal = 0)
	private static float[] useHoneyBeaconColour(float[] original) {
		if (stainedBlock instanceof HoneyBlock) {
			stainedBlock = null;
			return new float[]{(float) 199 / 255.0F, (float) 132 / 255.0F, (float) 34 / 255.0F};
		}
		return original;
	}

	@ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"), ordinal = 0)
	private static Block cacheStainedBlock(Block original) {
		stainedBlock = original;
		return original;
	}

	@ModifyVariable(method = "applyPlayerEffects", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/Box;stretch(DDD)Lnet/minecraft/util/math/Box;"))
	private static Box applyBeeconEffects(Box beeconRange, World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, @Nullable StatusEffect secondaryEffect) {
		boolean isBeecon = false;
		for (int m = 1; m < 10 && pos.getY() <= world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ()); ++m) {
			BlockState blockState = world.getBlockState(pos.up(m));
			Block block = blockState.getBlock();
			if (block instanceof HoneyBlock) {
				isBeecon = true;
				break;
			}
		}
		if (isBeecon) {
			List<BeeEntity> bees = world.getNonSpectatingEntities(BeeEntity.class, beeconRange);
			int j = (9 + beaconLevel * 2) * 20;
			int i = beaconLevel >= 4 && primaryEffect == secondaryEffect ? 1 : 0;
			for (BeeEntity bee : bees) {
				bee.addStatusEffect(new StatusEffectInstance(primaryEffect, j, i, true, true));
				if (beaconLevel >= 4 && primaryEffect != secondaryEffect && secondaryEffect != null) {
					bee.addStatusEffect(new StatusEffectInstance(secondaryEffect, j, 0, true, true));
				}
			}
		}

		return beeconRange;
	}
}
