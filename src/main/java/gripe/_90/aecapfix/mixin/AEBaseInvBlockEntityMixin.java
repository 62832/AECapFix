package gripe._90.aecapfix.mixin;

import appeng.blockentity.AEBaseInvBlockEntity;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AEBaseInvBlockEntity.class, remap = false)
public abstract class AEBaseInvBlockEntityMixin extends BlockEntity {
    @Unique
    private final Set<LazyOptional<IItemHandler>> aecapfix$itemHandlers = new HashSet<>();

    public AEBaseInvBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "getCapability",
            at = @At("RETURN"),
            slice =
                    @Slice(
                            to =
                                    @At(
                                            value = "INVOKE",
                                            target =
                                                    "Lappeng/blockentity/AEBaseBlockEntity;getCapability(Lnet/minecraftforge/common/capabilities/Capability;Lnet/minecraft/core/Direction;)Lnet/minecraftforge/common/util/LazyOptional;")))
    private <T> void storeCapability(
            Capability<T> capability, Direction facing, CallbackInfoReturnable<LazyOptional<T>> cir) {
        aecapfix$itemHandlers.add(cir.getReturnValue().cast());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        aecapfix$itemHandlers.forEach(LazyOptional::invalidate);
        aecapfix$itemHandlers.clear();
    }
}
