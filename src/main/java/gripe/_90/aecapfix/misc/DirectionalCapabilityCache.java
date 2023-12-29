package gripe._90.aecapfix.misc;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.util.LazyOptional;

public class DirectionalCapabilityCache<C> {
    private final List<LazyOptional<C>> holders = NonNullList.withSize(7, LazyOptional.empty());

    public LazyOptional<C> getOrCache(Direction side, LazyOptional<?> toCache) {
        var index = side != null ? side.get3DDataValue() : 6;

        if (!holders.get(index).isPresent() && toCache.isPresent()) {
            holders.set(index, toCache.cast());
        }

        return holders.get(index);
    }

    public void invalidate() {
        holders.forEach(LazyOptional::invalidate);
        holders.clear();
    }
}
