package gripe._90.aecapfix.mixin.ae2;

import appeng.api.behaviors.GenericInternalInventory;
import appeng.helpers.iface.PatternProviderReturnInventory;
import gripe._90.aecapfix.AECapFix;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PatternProviderReturnInventory.class, remap = false)
public abstract class PatternProviderReturnInventoryMixin implements AECapFix.Invalidator {
    @SuppressWarnings("UnstableApiUsage")
    @Shadow
    @Final
    private LazyOptional<GenericInternalInventory> genericInternalInventory;

    @Override
    public void aecapfix$invalidate() {
        genericInternalInventory.invalidate();
    }
}
