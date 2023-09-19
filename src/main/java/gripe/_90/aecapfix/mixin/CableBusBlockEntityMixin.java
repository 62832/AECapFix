package gripe._90.aecapfix.mixin;

import appeng.api.parts.IPart;
import appeng.blockentity.networking.CableBusBlockEntity;
import gripe._90.aecapfix.AECapFix;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = CableBusBlockEntity.class, remap = false)
public abstract class CableBusBlockEntityMixin extends BlockEntity {
    public CableBusBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        for (var direction : Direction.values()) {
            var part = getPart(direction);

            if (part instanceof AECapFix.Invalidator invalidator) {
                invalidator.aecapfix$invalidate();
            }
        }
    }

    @Shadow
    public abstract IPart getPart(Direction side);
}
