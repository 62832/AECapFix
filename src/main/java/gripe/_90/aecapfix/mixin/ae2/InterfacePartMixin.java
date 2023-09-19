package gripe._90.aecapfix.mixin.ae2;

import appeng.helpers.InterfaceLogic;
import appeng.parts.misc.InterfacePart;
import gripe._90.aecapfix.AECapFix;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = InterfacePart.class, remap = false)
public abstract class InterfacePartMixin implements AECapFix.Invalidator {
    @Shadow
    @Final
    private InterfaceLogic logic;

    @Override
    public void aecapfix$invalidate() {
        ((AECapFix.Invalidator) logic).aecapfix$invalidate();
    }
}
