package garden.hestia.pollinators_paradise.item;

import net.minecraft.item.FishingRodItem;

public class HoneyableFishingRodItem extends FishingRodItem implements Honeyable {
	public HoneyableFishingRodItem(Settings settings) {
		super(settings);
	}

	@Override
	public int bottleCapacity() {
		return 0;
	}

	@Override
	public int bottlePoints() {
		return 0;
	}
}
