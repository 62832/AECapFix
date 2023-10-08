package gripe._90.aecapfix.mixin.ae2;

import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderReturnInventory;
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
