package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.HoneyTypes;
import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {
	@Unique
	private final List<Entity> dodgeSuccesses = new ArrayList<>();
	@Unique
	private final List<Entity> dodgeFails = new ArrayList<>();

	public ProjectileEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Inject(method = "canHit", at = @At("RETURN"), cancellable = true)
	protected void chorusSuitDodge(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			// Previously Dodged
			if (dodgeSuccesses.contains(entity)) {
				cir.setReturnValue(false);
				cir.cancel();
				return;
			} else if (dodgeFails.contains(entity)) {
				return;
			}

			// Chance Dodge
			if (entity instanceof PlayerEntity player && entity.getWorld() instanceof ServerWorld sw && player instanceof ServerPlayerEntity spe &&
					player.getEquippedStack(EquipmentSlot.CHEST).isOf(PollinatorsParadise.APIARIST_SUIT)
					&& player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof Honeyable honeyableItem
					&& honeyableItem.getHoneyType(player.getEquippedStack(EquipmentSlot.CHEST)) == HoneyTypes.CHORUS) {
				if (this.random.nextBoolean()) {
					honeyableItem.decrementHoneyLevel(player.getEquippedStack(EquipmentSlot.CHEST), HoneyTypes.CHORUS);
					sw.playSound(null, spe.getX(), spe.getY(), spe.getZ(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, spe.getSoundCategory(), 1.0f, 1.0f);
					sw.emitGameEvent(GameEvent.TELEPORT, spe.getPos(), GameEvent.Context.create(spe));
					dodgeSuccesses.add(entity);
					cir.setReturnValue(false);
					cir.cancel();
				} else {
					dodgeFails.add(entity);
				}
			}
		}
	}
}
