package gripe._90.aecapfix.mixin.powah;

import gripe._90.aecapfix.misc.DirectionalCapabilityCache;
import gripe._90.aecapfix.misc.PowahEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owmii.powah.Powah;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.forge.ForgeEnvHandler;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;

@Mixin(value = ForgeEnvHandler.class, remap = false)
public abstract class ForgeEnvHandlerMixin {
    @Inject(method = "lambda$registerTransfer$3", at = @At("HEAD"), cancellable = true)
    private void registerCapabilities(AttachCapabilitiesEvent<BlockEntity> event, CallbackInfo ci) {
        ci.cancel();

        if (event.getObject() instanceof ReactorPartTile reactorPart) {
            var provider = new ICapabilityProvider() {
                private final DirectionalCapabilityCache<Object> cache = new DirectionalCapabilityCache<>();

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return reactorPart.core().isEmpty() || cap == ForgeCapabilities.ENERGY && !reactorPart.isExtractor()
                            ? LazyOptional.empty()
                            : cache.getOrCache(side, reactorPart.core().get().getCapability(cap, side))
                                    .cast();
                }

                private void invalidate() {
                    cache.invalidate();
                }
            };

            event.addCapability(Powah.id("reactor_part"), provider);
            event.addListener(provider::invalidate);
        }

        if (event.getObject() instanceof AbstractEnergyStorage<?, ?> energyStorage) {
            var provider = new ICapabilityProvider() {
                private final DirectionalCapabilityCache<IEnergyStorage> cache = new DirectionalCapabilityCache<>();

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap == ForgeCapabilities.ENERGY && energyStorage.isEnergyPresent(side)) {
                        var holder = LazyOptional.of(() -> new PowahEnergyStorage(energyStorage, side));
                        return cache.getOrCache(side, holder).cast();
                    }

                    return LazyOptional.empty();
                }

                private void invalidate() {
                    cache.invalidate();
                }
            };

            event.addCapability(Powah.id("energy"), provider);
            event.addListener(provider::invalidate);
        }

        if (event.getObject() instanceof IInventoryHolder invHolder) {
            var provider = new ICapabilityProvider() {
                private final LazyOptional<IItemHandler> holder = LazyOptional.of(
                                () -> invHolder.getInventory().getPlatformWrapper())
                        .cast();

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap == ForgeCapabilities.ITEM_HANDLER
                            && !invHolder.getInventory().isBlank()) {
                        return holder.cast();
                    }

                    return LazyOptional.empty();
                }

                private void invalidate() {
                    holder.invalidate();
                }
            };

            event.addCapability(Powah.id("inv"), provider);
            event.addListener(provider::invalidate);
        }

        if (event.getObject() instanceof ITankHolder tankHolder) {
            var provider = new ICapabilityProvider() {
                private final LazyOptional<IFluidHandler> holder = LazyOptional.of(
                                () -> tankHolder.getTank().getPlatformWrapper())
                        .cast();

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, holder.cast());
                }

                private void invalidate() {
                    holder.invalidate();
                }
            };

            event.addCapability(Powah.id("tank"), provider);
            event.addListener(provider::invalidate);
        }
    }
}
