package gripe._90.aecapfix.mixin.ae2;

import appeng.api.parts.IPartItem;
import appeng.parts.p2p.CapabilityP2PTunnelPart;
import appeng.parts.p2p.P2PTunnelPart;
import gripe._90.aecapfix.AECapFix;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CapabilityP2PTunnelPart.class, remap = false)
public abstract class CapabilityP2PTunnelPartMixin<P extends P2PTunnelPart<P>, A> extends P2PTunnelPart<P>
        implements AECapFix.Invalidator {
    @Shadow
    @Final
    private Capability<A> capability;

    @Shadow
    protected A inputHandler;

    @Shadow
    protected A outputHandler;

    @Unique
    private final LazyOptional<A> aecapfix$inputOptional = LazyOptional.of(() -> inputHandler);

    @Unique
    private final LazyOptional<A> aecapfix$outputOptional = LazyOptional.of(() -> outputHandler);

    public CapabilityP2PTunnelPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, CallbackInfoReturnable<LazyOptional<T>> cir) {
        cir.setReturnValue(capability.orEmpty(cap, isOutput() ? aecapfix$outputOptional : aecapfix$inputOptional));
    }

    @Override
    public void aecapfix$invalidate() {
        aecapfix$inputOptional.invalidate();
        aecapfix$outputOptional.invalidate();
    }
}
