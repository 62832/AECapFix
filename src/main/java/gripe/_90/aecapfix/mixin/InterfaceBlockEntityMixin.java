package gripe._90.aecapfix.mixin;

import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.helpers.InterfaceLogic;
import gripe._90.aecapfix.AECapFix;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = InterfaceBlockEntity.class, remap = false)
public abstract class InterfaceBlockEntityMixin extends BlockEntity {
    @Shadow
    @Final
    private InterfaceLogic logic;

    public InterfaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        ((AECapFix.Invalidator) logic).aecapfix$invalidate();
    }
}
