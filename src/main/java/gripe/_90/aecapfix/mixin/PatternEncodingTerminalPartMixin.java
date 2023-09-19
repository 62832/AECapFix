package gripe._90.aecapfix.mixin;

import appeng.parts.encoding.PatternEncodingLogic;
import appeng.parts.encoding.PatternEncodingTerminalPart;
import gripe._90.aecapfix.AECapFix;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PatternEncodingTerminalPart.class, remap = false)
public abstract class PatternEncodingTerminalPartMixin implements AECapFix.Invalidator {
    @Shadow
    @Final
    private PatternEncodingLogic logic;

    @Unique
    private final LazyOptional<IItemHandler> aecapfix$patternSlot =
            LazyOptional.of(() -> logic.getBlankPatternInv().toItemHandler());

    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true)
    private <T> void setCapability(Capability<T> cap, CallbackInfoReturnable<LazyOptional<T>> cir) {
        cir.setReturnValue(ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, aecapfix$patternSlot));
    }

    @Override
    public void aecapfix$invalidate() {
        aecapfix$patternSlot.invalidate();
    }
}
