package gripe._90.aecapfix.mixin.ae2;

import appeng.api.behaviors.GenericInternalInventory;
import appeng.capabilities.Capabilities;
import appeng.helpers.InterfaceLogic;
import appeng.util.ConfigInventory;
import gripe._90.aecapfix.AECapFix;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = InterfaceLogic.class, remap = false)
public abstract class InterfaceLogicMixin implements AECapFix.Invalidator {
    @Shadow
    @Final
    private ConfigInventory storage;

    @SuppressWarnings("UnstableApiUsage")
    @Unique
    private final LazyOptional<GenericInternalInventory> aecapfix$inventory = LazyOptional.of(() -> storage);

    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == Capabilities.GENERIC_INTERNAL_INV) {
            cir.setReturnValue(aecapfix$inventory.cast());
        }
    }

    @Override
    public void aecapfix$invalidate() {
        aecapfix$inventory.invalidate();
    }
}
