package gripe._90.aecapfix.mixin.ae2;

import appeng.blockentity.powersink.AEBasePoweredBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AEBasePoweredBlockEntity.class, remap = false)
public abstract class AEBasePoweredBlockEntityMixin extends BlockEntity {
    @Shadow
    @Final
    private LazyOptional<IEnergyStorage> forgeEnergyAdapterOptional;

    public AEBasePoweredBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        forgeEnergyAdapterOptional.invalidate();
    }
}
