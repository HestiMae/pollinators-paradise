package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import garden.hestia.pollinators_paradise.item.HoneyableShearsItem;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity {

	protected BeehiveBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	public static void dropHoneycomb(World world, BlockPos pos) {
	}

	@Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BeehiveBlock;dropHoneycomb(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
	void apiaristShearsDoubleDrops(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (state.getBlock() == Blocks.BEE_NEST && itemStack.getItem() instanceof HoneyableShearsItem honeyableShearsItem) {
			if (honeyableShearsItem.decrementHoneyLevel(itemStack, Honeyable.HoneyType.HONEY))
			{
				dropHoneycomb(world, pos);
			}
			if (honeyableShearsItem.decrementHoneyLevel(itemStack, Honeyable.HoneyType.CHORUS) && !world.isClient())
			{
				LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder((ServerWorld)world)
						.add(LootContextParameters.BLOCK_STATE, state)
						.add(LootContextParameters.ORIGIN, pos.ofCenter())
						.add(LootContextParameters.TOOL, itemStack)
						.add(LootContextParameters.THIS_ENTITY, player)
						.add(LootContextParameters.BLOCK_ENTITY, world.getBlockEntity(pos))
						.withLuck(player.getLuck())
						.build(LootContextTypes.BLOCK);

				Identifier biomeID = world.getRegistryManager().get(RegistryKeys.BIOME).getId(world.getBiome(pos).value());
				LootTable lootTable = world.getServer().getLootManager().getLootTable(new Identifier(PollinatorsParadise.ID, "chorus_shearing/%s/%s".formatted(biomeID.getNamespace(), biomeID.getPath())));
				if (lootTable == LootTable.EMPTY) lootTable = world.getServer().getLootManager().getLootTable(new Identifier(PollinatorsParadise.ID, "chorus_shearing"));
				List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
				for (ItemStack stack : list) {
					dropStack(world, pos, stack);
				}
			}
		}
	}

	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;isLitCampfireInRange(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"))
	boolean apiaristShearsBypassCampfire(World world, BlockPos pos, BlockState state, World world2, BlockPos pos2, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return (player.getStackInHand(hand).getItem() instanceof HoneyableShearsItem honeyableShearsItem
				&& honeyableShearsItem.getHoneyType(player.getStackInHand(hand)) != Honeyable.HoneyType.NONE)
				|| CampfireBlock.isLitCampfireInRange(world, pos);
	}
}
