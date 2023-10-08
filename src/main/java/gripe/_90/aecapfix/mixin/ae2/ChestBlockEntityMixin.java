package gripe._90.aecapfix.mixin.ae2;

import appeng.blockentity.storage.ChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChestBlockEntity.class, remap = false)
public abstract class ChestBlockEntityMixin extends BlockEntity {
    @Shadow
    private IFluidHandler fluidHandler;

    @Unique
    private final LazyOptional<IFluidHandler> aecapfix$fluidHandler = LazyOptional.of(() -> fluidHandler);

    public ChestBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            cir.setReturnValue(aecapfix$fluidHandler.cast());
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        aecapfix$fluidHandler.invalidate();
    }
}
