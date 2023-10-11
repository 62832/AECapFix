package gripe._90.aecapfix.mixin.ae2;

import appeng.api.inventories.InternalInventory;
import appeng.blockentity.misc.CondenserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CondenserBlockEntity.class, remap = false)
public abstract class CondenserBlockEntityMixin extends BlockEntity {
    @Shadow
    @Final
    private InternalInventory externalInv;

    @Shadow
    @Final
    private IFluidHandler fluidHandler;

    @Unique
    private final LazyOptional<IItemHandler> aecapfix$externalInv = LazyOptional.of(() -> externalInv.toItemHandler());

    @Unique
    private final LazyOptional<IFluidHandler> aecapfix$fluidHandler = LazyOptional.of(() -> fluidHandler);

    public CondenserBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            cir.setReturnValue(aecapfix$externalInv.cast());
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            cir.setReturnValue(aecapfix$fluidHandler.cast());
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        aecapfix$externalInv.invalidate();
        aecapfix$fluidHandler.invalidate();
    }
}
