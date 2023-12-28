package gripe._90.aecapfix.mixin.ae2;

import appeng.capabilities.Capabilities;
import appeng.core.AppEng;
import appeng.helpers.externalstorage.GenericStackFluidStorage;
import appeng.helpers.externalstorage.GenericStackItemStorage;
import appeng.init.InitCapabilities;
import gripe._90.aecapfix.misc.DirectionalCapabilityCache;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InitCapabilities.class, remap = false)
public abstract class InitCapabilitiesMixin {
    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "registerGenericInvWrapper", at = @At("HEAD"), cancellable = true)
    private static void registerCapabilities(AttachCapabilitiesEvent<BlockEntity> event, CallbackInfo ci) {
        ci.cancel();

        var provider = new ICapabilityProvider() {
            private final BlockEntity be = event.getObject();

            private final DirectionalCapabilityCache<IItemHandler> itemHandlers = new DirectionalCapabilityCache<>();
            private final DirectionalCapabilityCache<IFluidHandler> fluidHandlers = new DirectionalCapabilityCache<>();

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    var holder = be.getCapability(Capabilities.GENERIC_INTERNAL_INV, side)
                            .lazyMap(GenericStackItemStorage::new);
                    return itemHandlers.getOrCache(side, holder).cast();
                } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
                    var holder = be.getCapability(Capabilities.GENERIC_INTERNAL_INV, side)
                            .lazyMap(GenericStackFluidStorage::new);
                    return fluidHandlers.getOrCache(side, holder).cast();
                } else {
                    return LazyOptional.empty();
                }
            }

            private void invalidate() {
                itemHandlers.invalidate();
                fluidHandlers.invalidate();
            }
        };

        event.addCapability(AppEng.makeId("generic_inv_wrapper"), provider);
        event.addListener(provider::invalidate);
    }
}
