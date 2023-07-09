package garden.hestia.pollinators_paradise.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author unascribed
 * @link <a href="https://git.sleeping.town/unascribed-mods/Yttr/src/branch/1.18.2/src/main/java/com/unascribed/yttr/inventory/SSDScreenHandler.java#L105">Yttr#SSDScreenHandler</a>
 * <p>
 * Overrides {@link ScreenHandler#insertItem(ItemStack, int, int, boolean)} to be slot-size aware.
 * This fixes a vanilla bug where stackable items like bottles don't behave when quick-transferred into a brewing stand.
 */
@Mixin(BrewingStandScreenHandler.class)
public abstract class BrewingStandScreenHandlerMixin extends ScreenHandler {
	protected BrewingStandScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Override
	protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
		boolean bl = false;
		int i = startIndex;
		if (fromLast) {
			i = endIndex - 1;
		}

		if (stack.isStackable()) {
			while (!stack.isEmpty()) {
				if (fromLast) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}

				Slot slot = this.slots.get(i);
				ItemStack itemStack = slot.getStack();
				int max = slot.getMaxItemCount(itemStack);
				if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
					int j = itemStack.getCount() + stack.getCount();
					if (j <= max) {
						stack.setCount(0);
						itemStack.setCount(j);
						slot.markDirty();
						bl = true;
					} else if (itemStack.getCount() < max) {
						stack.decrement(max - itemStack.getCount());
						itemStack.setCount(max);
						slot.markDirty();
						bl = true;
					}
				}

				if (fromLast) {
					--i;
				} else {
					++i;
				}
			}
		}

		if (!stack.isEmpty()) {
			if (fromLast) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while(true) {
				if (fromLast) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}

				Slot slot = this.slots.get(i);
				ItemStack itemStack = slot.getStack();
				int max = slot.getMaxItemCount(itemStack);
				if (itemStack.isEmpty() && slot.canInsert(stack)) {
					if (stack.getCount() > max) {
						slot.setStack(stack.split(max));
					} else {
						slot.setStack(stack.split(stack.getCount()));
					}

					slot.markDirty();
					bl = true;
					break;
				}

				if (fromLast) {
					--i;
				} else {
					++i;
				}
			}
		}

		return bl;
	}


}
