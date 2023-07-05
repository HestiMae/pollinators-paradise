package garden.hestia.pollinators_paradise.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ChorusHoneyBottleItem extends HoneyBottleItem {

	public ChorusHoneyBottleItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack returnStack = super.finishUsing(stack, world, user);
		if (!world.isClient) {
			Vec3d pos = user.getPos();
			double oldX = pos.getX();
			double oldY = pos.getY();
			double oldZ = pos.getZ();

			if (user.hasVehicle()) {
				user.stopRiding();
			}
			if (user instanceof PlayerEntity) {
				((PlayerEntity)user).getItemCooldownManager().set(this, 20);
			}
			int ceilingY = 0;
			for (int i = 1; i < 8; i++)
			{
				if (!world.getBlockState(user.getBlockPos().up(i)).getCollisionShape(world, user.getBlockPos().up(i)).isEmpty())
				{
					ceilingY = i + 1;
					break;
				}
			}
			for(int newY = Math.max(user.getBlockY() + ceilingY, world.getBottomY()); newY < world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1; ++newY) {

				if (user.teleport(oldX, newY, oldZ, true)) {
					world.emitGameEvent(GameEvent.TELEPORT, pos, GameEvent.Context.create(user));
					SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
					world.playSound(null, oldX, oldY, oldZ, soundEvent, SoundCategory.PLAYERS, 1.0F, 0.8F);
					user.playSound(soundEvent, 1.0F, 0.8F);
					break;
				}
			}


		}

		return returnStack;
	}
}
