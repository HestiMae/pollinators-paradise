package garden.hestia.pollinators_paradise.mixin;

import garden.hestia.pollinators_paradise.PollinatorsParadise;
import garden.hestia.pollinators_paradise.item.Honeyable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
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
	private static final double CALMING_BOX_SIZE = 10;
	private static final double ALLY_BOX_SIZE = 30;
	private static final double ATTACKER_BOX_SIZE = 16;
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Inject(method = "tick", at = @At(value = "TAIL"))
	public void tick(CallbackInfo ci) {
		ItemStack helmetStack = getEquippedStack(EquipmentSlot.HEAD);
		if (helmetStack.isOf(PollinatorsParadise.APIARIST_VEIL) &&
				helmetStack.getItem() instanceof Honeyable honeyItem && helmetStack.getCooldown() == 0) {
			Vec3d pos = this.getPos();
			if (honeyItem.getHoneyType(helmetStack) == Honeyable.HoneyType.HONEY) {
				List<BeeEntity> calmableBees = this.getWorld().getNonSpectatingEntities(BeeEntity.class, Box.of(pos, CALMING_BOX_SIZE, CALMING_BOX_SIZE, CALMING_BOX_SIZE)).stream().filter(y -> y.getAngryAt() == this.uuid).toList();
				if (!calmableBees.isEmpty() && honeyItem.decrementHoneyLevel(helmetStack, Honeyable.HoneyType.HONEY)) {
					for (BeeEntity calmableBee : calmableBees) {
						calmableBee.stopAnger();
					}
					helmetStack.setCooldown(20);
				}
			} else if (honeyItem.getHoneyType(helmetStack) == Honeyable.HoneyType.CHORUS) {
				List<BeeEntity> allyBees = this.getWorld().getNonSpectatingEntities(BeeEntity.class, Box.of(pos, ALLY_BOX_SIZE, ALLY_BOX_SIZE, ALLY_BOX_SIZE)).stream().filter(b -> !b.hasAngerTime() && !b.hasStung()).toList();
				List<HostileEntity> attackers = this.getWorld().getNonSpectatingEntities(HostileEntity.class, Box.of(pos, ATTACKER_BOX_SIZE, ATTACKER_BOX_SIZE, ATTACKER_BOX_SIZE)).stream().filter(h -> h.isAngryAt((PlayerEntity) (Object) this)).toList();
				if (!allyBees.isEmpty() && !attackers.isEmpty()) {
					if (allyBees.stream().anyMatch(allyBee -> PollinatorsParadise.safeBeeAnger(allyBee, attackers.get(allyBee.getRandom().nextInt(attackers.size()))))) {
						honeyItem.decrementHoneyLevel(helmetStack, Honeyable.HoneyType.CHORUS);
						helmetStack.setCooldown(100);
					}
				}
			}
		}

	}
}
