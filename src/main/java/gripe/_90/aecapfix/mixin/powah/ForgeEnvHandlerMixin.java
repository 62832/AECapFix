package gripe._90.aecapfix.mixin.powah;

import gripe._90.aecapfix.misc.PowahEnergyStorage;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
                private final Set<LazyOptional<IEnergyStorage>> holders = new HashSet<>();

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap == ForgeCapabilities.ENERGY) {
                        if (reactorPart.core().isPresent() && reactorPart.isExtractor()) {
                            var holder = reactorPart.core().get().getCapability(cap, side);
                            holders.add(holder.cast());
                            return holder;
                        }
                    }

                    return LazyOptional.empty();
                }

                private void invalidate() {
                    holders.forEach(LazyOptional::invalidate);
                    holders.clear();
                }
            };

            event.addCapability(Powah.id("reactor_part"), provider);
            event.addListener(provider::invalidate);
        }

        if (event.getObject() instanceof AbstractEnergyStorage<?, ?> energyStorage) {
            var provider = new ICapabilityProvider() {
                private final Set<LazyOptional<PowahEnergyStorage>> holders = new HashSet<>();

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap == ForgeCapabilities.ENERGY && energyStorage.isEnergyPresent(side)) {
                        var holder = LazyOptional.of(() -> new PowahEnergyStorage(energyStorage, side));
                        holders.add(holder);
                        return holder.cast();
                    }

                    return LazyOptional.empty();
                }

                private void invalidate() {
                    holders.forEach(LazyOptional::invalidate);
                    holders.clear();
                }
            };

            event.addCapability(Powah.id("energy"), provider);
            event.addListener(provider::invalidate);
        }

        if (event.getObject() instanceof IInventoryHolder invHolder) {
            var provider = new ICapabilityProvider() {
                private final LazyOptional<Object> holder =
                        LazyOptional.of(() -> invHolder.getInventory().getPlatformWrapper());

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, holder.cast());
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
                private final LazyOptional<Object> holder =
                        LazyOptional.of(() -> tankHolder.getTank().getPlatformWrapper());

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
