package gripe._90.aecapfix.misc;

import com.google.common.primitives.Ints;
import net.minecraft.core.Direction;
import net.minecraftforge.energy.IEnergyStorage;
import owmii.powah.lib.block.AbstractEnergyStorage;

public record PowahEnergyStorage(AbstractEnergyStorage<?, ?> storage, Direction side) implements IEnergyStorage {
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return Ints.saturatedCast(storage.receiveEnergy(maxReceive, simulate, side));
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return Ints.saturatedCast(storage.extractEnergy(maxExtract, simulate, side));
    }

    @Override
    public int getEnergyStored() {
        return Ints.saturatedCast(storage.getEnergy().getEnergyStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return Ints.saturatedCast(storage.getEnergy().getMaxEnergyStored());
    }

    @Override
    public boolean canExtract() {
        return storage.canExtractEnergy(side);
    }

    @Override
    public boolean canReceive() {
        return storage.canReceiveEnergy(side);
    }
}
