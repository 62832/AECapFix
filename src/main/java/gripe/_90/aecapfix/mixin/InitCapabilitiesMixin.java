package gripe._90.aecapfix.mixin;

import appeng.capabilities.Capabilities;
import appeng.core.AppEng;
import appeng.helpers.externalstorage.GenericStackFluidStorage;
import appeng.helpers.externalstorage.GenericStackItemStorage;
import appeng.init.InitCapabilities;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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

            private final Set<LazyOptional<GenericStackItemStorage>> itemHandlers = new HashSet<>();
            private final Set<LazyOptional<GenericStackFluidStorage>> fluidHandlers = new HashSet<>();

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    var handler = be.getCapability(Capabilities.GENERIC_INTERNAL_INV, side)
                            .lazyMap(GenericStackItemStorage::new);
                    itemHandlers.add(handler);
                    return handler.cast();
                } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
                    var handler = be.getCapability(Capabilities.GENERIC_INTERNAL_INV, side)
                            .lazyMap(GenericStackFluidStorage::new);
                    fluidHandlers.add(handler);
                    return handler.cast();
                } else {
                    return LazyOptional.empty();
                }
            }

            private void invalidate() {
                itemHandlers.forEach(LazyOptional::invalidate);
                fluidHandlers.forEach(LazyOptional::invalidate);

                itemHandlers.clear();
                fluidHandlers.clear();
            }
        };

        event.addCapability(AppEng.makeId("generic_inv_wrapper"), provider);
        event.addListener(provider::invalidate);
    }
}
