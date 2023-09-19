package gripe._90.aecapfix.mixin;

import appeng.helpers.iface.PatternProviderLogic;
import appeng.helpers.iface.PatternProviderReturnInventory;
import gripe._90.aecapfix.AECapFix;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PatternProviderLogic.class, remap = false)
public abstract class PatternProviderLogicMixin implements AECapFix.Invalidator {
    @Shadow
    @Final
    private PatternProviderReturnInventory returnInv;

    @Override
    public void aecapfix$invalidate() {
        ((AECapFix.Invalidator) returnInv).aecapfix$invalidate();
    }
}
