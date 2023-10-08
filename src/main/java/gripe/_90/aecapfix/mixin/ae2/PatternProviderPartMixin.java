package gripe._90.aecapfix.mixin.ae2;

import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.parts.crafting.PatternProviderPart;
import gripe._90.aecapfix.AECapFix;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PatternProviderPart.class, remap = false)
public abstract class PatternProviderPartMixin implements AECapFix.Invalidator {
    @Shadow
    @Final
    protected PatternProviderLogic logic;

    @Override
    public void aecapfix$invalidate() {
        ((AECapFix.Invalidator) logic).aecapfix$invalidate();
    }
}
