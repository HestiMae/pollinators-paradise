package garden.hestia.pollinators_paradise;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;

import java.util.Set;

public record HoneyType(int itemBarColor, int itemTintColor, float[] armorColor, Item bottleItem) {
	private static final BiMap<String, HoneyType> registry = HashBiMap.create();

	public static HoneyType register(String name, HoneyType honeyType)
	{
		if (registry.containsKey(name)) throw new IllegalArgumentException("A HoneyType with that name already exists: " + name);
		registry.put(name, honeyType);
		return honeyType;
	}

	public static HoneyType ofName(String name)
	{
		if (!registry.containsKey(name))
		{
			throw new IllegalArgumentException("A HoneyType with that name does not exist: " + name);
		}
		return registry.get(name);
	}

	public static String getName(HoneyType honeyType)
	{
		if (!registry.containsValue(honeyType))
		{
			throw new IllegalArgumentException("That HoneyType is not registered: " + honeyType);
		}
		return registry.inverse().get(honeyType);
	}

	public static Set<HoneyType> values()
	{
		return registry.values();
	}
}
