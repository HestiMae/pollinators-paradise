package garden.hestia.pollinators_paradise.mixin;

import net.minecraft.block.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("deprecation")
@Mixin(PointedDripstoneBlock.class)
public class PointedDripstoneBlockMixin extends Block  {
	public PointedDripstoneBlockMixin(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext && entityShapeContext.getEntity() instanceof BeeEntity) return VoxelShapes.empty();
		return super.getCollisionShape(state, world, pos, context);
	}
}
