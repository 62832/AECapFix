package gripe._90.aecapfix.mixin.ae2;

import appeng.helpers.ForgeEnergyAdapter;
import appeng.parts.networking.EnergyAcceptorPart;
import gripe._90.aecapfix.AECapFix;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EnergyAcceptorPart.class, remap = false)
public abstract class EnergyAcceptorPartMixin implements AECapFix.Invalidator {
    @Shadow
    private LazyOptional<ForgeEnergyAdapter> forgeEnergyAdapterOptional;

    @Override
    public void aecapfix$invalidate() {
        forgeEnergyAdapterOptional.invalidate();
    }
}
