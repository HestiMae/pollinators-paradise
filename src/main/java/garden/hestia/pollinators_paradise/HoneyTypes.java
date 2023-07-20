package garden.hestia.pollinators_paradise;

import net.minecraft.item.Items;


public class HoneyTypes {
	public static final HoneyType CHORUS = HoneyType.register("CHORUS", new HoneyType(0xff8f31aa, 0xff8f31aa, new float[]{0.66F, 0.41F, 0.73F}, PollinatorsParadise.CHORUS_HONEY_BOTTLE));
	public static final HoneyType HONEY = HoneyType.register("HONEY", new HoneyType(0xffff9116, 0xffff9116, new float[]{247 / 255.0F, 178 / 255.0F, 74 / 255.0F}, Items.HONEY_BOTTLE));

	public static void initialize() {

	}
}
