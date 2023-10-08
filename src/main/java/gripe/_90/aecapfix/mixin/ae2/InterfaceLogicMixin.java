package gripe._90.aecapfix.mixin.ae2;

import appeng.api.behaviors.GenericInternalInventory;
import appeng.api.networking.IManagedGridNode;
import appeng.api.storage.MEStorage;
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
    private MEStorage networkStorage;

    @SuppressWarnings("UnstableApiUsage")
    @Unique
    private LazyOptional<GenericInternalInventory> aecapfix$inventory;

    @Unique
    private final LazyOptional<MEStorage> aecapfix$storage = LazyOptional.of(() -> networkStorage);

    @Inject(
            method =
                    "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/InterfaceLogicHost;Lnet/minecraft/world/item/Item;I)V",
            at = @At("RETURN"))
    private void setInventory(IManagedGridNode gridNode, InterfaceLogicHost host, Item is, int slots, CallbackInfo ci) {
        aecapfix$inventory = LazyOptional.of(() -> storage);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == Capabilities.GENERIC_INTERNAL_INV) {
            cir.setReturnValue(aecapfix$inventory.cast());
        } else if (cap == Capabilities.STORAGE) {
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
