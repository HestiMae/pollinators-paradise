package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.item.HoneyableShearsItem;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {

	@Shadow
	public static void dropHoneycomb(World world, BlockPos pos) {
	}

	@Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BeehiveBlock;dropHoneycomb(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
	void apiaristShearsDoubleDrops(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		if (state.getBlock() == Blocks.BEE_NEST && itemStack.getItem() instanceof HoneyableShearsItem honeyableShearsItem && honeyableShearsItem.getHoneyLevel(itemStack) > 0)
		{
			dropHoneycomb(world, pos);
		}
	}

	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;isLitCampfireInRange(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"))
	boolean apiaristShearsBypassCampfire(World world, BlockPos pos, BlockState state, World world2, BlockPos pos2, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		return (player.getStackInHand(hand).getItem() instanceof HoneyableShearsItem honeyableShearsItem
				&& honeyableShearsItem.getHoneyLevel(player.getStackInHand(hand)) > 0)
				|| CampfireBlock.isLitCampfireInRange(world, pos);
	}
}
