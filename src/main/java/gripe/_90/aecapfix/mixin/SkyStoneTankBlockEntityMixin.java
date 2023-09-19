package gripe._90.aecapfix.mixin;

import appeng.blockentity.storage.SkyStoneTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SkyStoneTankBlockEntity.class, remap = false)
public abstract class SkyStoneTankBlockEntityMixin extends BlockEntity {
    @Shadow
    @Final
    private LazyOptional<IFluidHandler> holder;

    public SkyStoneTankBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        holder.invalidate();
    }
}
