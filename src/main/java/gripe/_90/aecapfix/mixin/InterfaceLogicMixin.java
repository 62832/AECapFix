package gripe._90.aecapfix.mixin;

import appeng.api.behaviors.GenericInternalInventory;
import appeng.api.networking.IManagedGridNode;
import appeng.api.storage.IStorageMonitorableAccessor;
import appeng.capabilities.Capabilities;
import appeng.helpers.InterfaceLogic;
import appeng.helpers.InterfaceLogicHost;
import appeng.util.ConfigInventory;
import gripe._90.aecapfix.AECapFix;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = InterfaceLogic.class, remap = false)
public abstract class InterfaceLogicMixin implements AECapFix.Invalidator {
    @Shadow
    @Final
    private ConfigInventory storage;

    @Shadow
    @Final
    private IStorageMonitorableAccessor accessor;

    @SuppressWarnings("UnstableApiUsage")
    @Unique
    private LazyOptional<GenericInternalInventory> aecapfix$inventory;

    @Unique
    private final LazyOptional<IStorageMonitorableAccessor> aecapfix$storage = LazyOptional.of(() -> accessor);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setInventory(IManagedGridNode gridNode, InterfaceLogicHost host, Item is, CallbackInfo ci) {
        aecapfix$inventory = LazyOptional.of(() -> storage);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == Capabilities.GENERIC_INTERNAL_INV) {
            cir.setReturnValue(aecapfix$inventory.cast());
        } else if (cap == Capabilities.STORAGE_MONITORABLE_ACCESSOR) {
            cir.setReturnValue(aecapfix$storage.cast());
        } else {
            cir.setReturnValue(LazyOptional.empty());
        }
    }

    @Override
    public void aecapfix$invalidate() {
        aecapfix$inventory.invalidate();
        aecapfix$storage.invalidate();
    }
}
