package garden.hestia.pollinators_paradise.base;

import com.google.common.collect.Multiset;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

public class HoneyColorProvider implements ItemColorProvider {
	public static Multiset<HoneyType> honeyQuarters = null;
	public static final int OVERLAY_LAYERS = 5;

	@Override
	public int getColor(ItemStack itemStack, int tintIndex) {
		if (tintIndex > 0 && honeyQuarters != null) {
			int quarterIndex = Math.round((float) ((honeyQuarters.size() - 1) * (tintIndex - 1)) / (OVERLAY_LAYERS - 1));
			for (HoneyType honeyType : honeyQuarters) {
				if (quarterIndex == 0) return honeyType.itemTintColor();
				quarterIndex--;
			}
		}
		return -1;
	}
}
