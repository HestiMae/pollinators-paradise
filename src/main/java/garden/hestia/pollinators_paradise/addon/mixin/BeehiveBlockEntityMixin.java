package garden.hestia.pollinators_paradise.addon.mixin;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveBlockEntityMixin {
	@ModifyArg(method = "addBee", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BeehiveBlockEntity$Bee;<init>(Lnet/minecraft/nbt/NbtCompound;II)V"), index = 2)
	private int applyBeeHasteInHive(NbtCompound nbtCompound, int ticksInHive, int minOccupationTime) {
		int hasteLevels = 0;
		if (nbtCompound.contains("ActiveEffects", NbtElement.LIST_TYPE)) {
			for (NbtElement e : nbtCompound.getList("ActiveEffects", NbtElement.COMPOUND_TYPE)) {
				if (e instanceof NbtCompound c && c.contains("quilt:id", NbtElement.STRING_TYPE) && c.getString("quilt:id").equals("minecraft:haste")) {
					hasteLevels = c.getInt("Amplifier") + 1;
					break;
				}
			}
		}
		return minOccupationTime / (1 + hasteLevels);
	}
}
