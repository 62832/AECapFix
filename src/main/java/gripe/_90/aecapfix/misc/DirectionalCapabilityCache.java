package gripe._90.aecapfix.misc;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;

public class DirectionalCapabilityCache<C> {
    private final Map<Direction, LazyOptional<C>> directional = new HashMap<>();
    private LazyOptional<C> nullDirection = LazyOptional.empty();

    public LazyOptional<C> getOrCache(Direction side, LazyOptional<?> toCache) {
        if (toCache.isPresent()) {
            if (side == null) {
                if (!nullDirection.isPresent()) {
                    nullDirection = toCache.cast();
                }

                return nullDirection;
            } else {
                directional.putIfAbsent(side, toCache.cast());
                return directional.get(side);
            }
        } else {
            return LazyOptional.empty();
        }
    }

    public void invalidate() {
        directional.forEach((direction, holder) -> holder.invalidate());
        directional.clear();
        nullDirection.invalidate();
    }
}
