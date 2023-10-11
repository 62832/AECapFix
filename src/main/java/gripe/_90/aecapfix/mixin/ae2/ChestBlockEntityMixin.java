package gripe._90.aecapfix.mixin.ae2;

import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.storage.ChestBlockEntity;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChestBlockEntity.class, remap = false)
public abstract class ChestBlockEntityMixin extends AEBaseBlockEntity {
    @Shadow
    private IFluidHandler fluidHandler;

    @Unique
    private IFluidHandler aecapfix$updatedFluidHandler;

    @Unique
    private LazyOptional<IFluidHandler> aecapfix$fluidHandlerHolder = LazyOptional.of(() -> fluidHandler);

    public ChestBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "updateHandler", at = @At("HEAD"))
    private void cacheFluidHandler(CallbackInfo ci) {
        aecapfix$updatedFluidHandler = fluidHandler;
    }

    @Inject(method = "updateHandler", at = @At("TAIL"))
    private void updateCapability(CallbackInfo ci) {
        if (!Objects.equals(fluidHandler, aecapfix$updatedFluidHandler)) {
            aecapfix$fluidHandlerHolder.invalidate();
            aecapfix$fluidHandlerHolder = LazyOptional.of(() -> fluidHandler);
        }
    }

    @Inject(
            method = "getCapability",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Lappeng/blockentity/storage/ChestBlockEntity;updateHandler()V",
                            shift = At.Shift.AFTER),
            cancellable = true)
    private <T> void setCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && fluidHandler != null && side != getForward()) {
            cir.setReturnValue(aecapfix$fluidHandlerHolder.cast());
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        aecapfix$fluidHandlerHolder.invalidate();
    }
}
