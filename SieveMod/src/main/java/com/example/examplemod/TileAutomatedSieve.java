package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileAutomatedSieve extends TileEntity implements ITickable {

    // Слот 0: Вход, Слот 1: Выход, Слот 2: Скорость, Слот 3: Шанс
    public final SieveItemHandler inventory = new SieveItemHandler();
    private final IItemHandler externalHandler = new SieveExternalHandler(inventory);
    private final Random rand = new Random();

    public final SieveEnergyStorage energy = new SieveEnergyStorage(50000, 1000);

    public int progress = 0;
    public int maxProgress = 100;
    private final int energyPerTick = 20;

    @Override
    public void update() {
        if (this.world == null || this.world.isRemote) return;

        ItemStack input = inventory.getStackInSlot(0);

        if (!input.isEmpty() && energy.getEnergyStored() >= energyPerTick) {
            Block inputBlock = Block.getBlockFromItem(input.getItem());
            if (inputBlock == Blocks.AIR || !SiftingRecipeRegistry.hasRecipe(inputBlock)) {
                slowResetProgress();
                return;
            }

            int speedUpgrades = countUpgrades(ItemSpeedUpgrade.class, 2);
            int speedMultiplier = 1 + speedUpgrades;

            ItemStack output = inventory.getStackInSlot(1);
            boolean outputBlocked = !output.isEmpty() && output.getCount() >= 64;
            if (outputBlocked) {
                slowResetProgress();
                return;
            }

            energy.extractEnergy(energyPerTick, false);
            progress += speedMultiplier;

            if (progress >= maxProgress) {
                processItem();
                progress = 0;
            }
            markDirty();
        } else {
            slowResetProgress();
        }
    }

    private void slowResetProgress() {
        if (progress > 0) {
            progress = Math.max(0, progress - 2);
            markDirty();
        }
    }

    private int countUpgrades(Class<?> upgradeClass, int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        if (!stack.isEmpty() && upgradeClass.isInstance(stack.getItem())) {
            return stack.getCount();
        }
        return 0;
    }

    private void processItem() {
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty()) return;

        Block inputBlock = Block.getBlockFromItem(input.getItem());
        SiftingRecipe recipe = SiftingRecipeRegistry.getRecipe(inputBlock);
        if (recipe == null) return;

        int chanceUpgrades = countUpgrades(ItemChanceUpgrade.class, 3);
        double luckModifier = 1.0 + (chanceUpgrades * 1.0);
        if (luckModifier > 4.0) luckModifier = 4.0;

        ItemStack result = recipe.getRandomOutput(rand, luckModifier);
        if (result.isEmpty()) {
            input.shrink(1);
            return;
        }

        ItemStack output = inventory.getStackInSlot(1);
        if (output.isEmpty()) {
            input.shrink(1);
            inventory.setStackInSlot(1, result);
        } else if (output.getItem() == result.getItem()
                && output.getCount() + result.getCount() <= 64) {
            input.shrink(1);
            output.grow(result.getCount());
        }
        // else: output blocked, do nothing — keep input for next cycle
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
            }
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(externalHandler);
        }
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energy);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
        progress = compound.getInteger("Progress");
        energy.setEnergyStored(compound.getInteger("Energy"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Inventory", inventory.serializeNBT());
        compound.setInteger("Progress", progress);
        compound.setInteger("Energy", energy.getEnergyStored());
        return compound;
    }

    public class SieveEnergyStorage extends EnergyStorage {
        public SieveEnergyStorage(int capacity, int maxTransfer) {
            super(capacity, maxTransfer);
        }

        public void setEnergyStored(int energy) {
            this.energy = Math.max(0, Math.min(energy, this.capacity));
            markDirty();
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int result = super.receiveEnergy(maxReceive, simulate);
            if (result > 0) markDirty();
            return result;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int result = super.extractEnergy(maxExtract, simulate);
            if (result > 0) markDirty();
            return result;
        }
    }

    public class SieveItemHandler extends ItemStackHandler {

        public SieveItemHandler() {
            super(4);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch (slot) {
                case 0:
                    Block block = Block.getBlockFromItem(stack.getItem());
                    return block != Blocks.AIR && SiftingRecipeRegistry.hasRecipe(block);
                case 1:
                    return false;
                case 2:
                    return stack.getItem() instanceof ItemSpeedUpgrade;
                case 3:
                    return stack.getItem() instanceof ItemChanceUpgrade;
                default:
                    return false;
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    }

    private static class SieveExternalHandler implements IItemHandler {
        private final SieveItemHandler internal;

        SieveExternalHandler(SieveItemHandler internal) {
            this.internal = internal;
        }

        @Override
        public int getSlots() {
            return internal.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return internal.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot != 0) return stack;
            return internal.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 1) return ItemStack.EMPTY;
            return internal.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return internal.getSlotLimit(slot);
        }
    }
}
