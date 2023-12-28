package gripe._90.aecapfix.mixin.ae2;

import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.iface.PatternProviderLogic;
import gripe._90.aecapfix.AECapFix;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PatternProviderBlockEntity.class, remap = false)
public abstract class PatternProviderBlockEntityMixin extends BlockEntity {
    @Shadow
    @Final
    protected PatternProviderLogic logic;

    public PatternProviderBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        ((AECapFix.Invalidator) logic).aecapfix$invalidate();
    }
}
