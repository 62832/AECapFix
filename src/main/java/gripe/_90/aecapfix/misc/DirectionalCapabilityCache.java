package gripe._90.aecapfix.misc;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;

public class DirectionalCapabilityCache<C> {
    private final Map<String, LazyOptional<C>> holders = new HashMap<>();

    public LazyOptional<C> getOrCache(Direction side, LazyOptional<?> toCache) {
        return holders.computeIfAbsent(side == null ? "null" : side.getName(), k -> toCache.cast());
    }

    public void invalidate() {
        holders.forEach((side, holder) -> holder.invalidate());
        holders.clear();
    }
}
