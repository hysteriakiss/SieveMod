package com.example.examplemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public class ContainerAutomatedSieve extends Container {

    private final TileAutomatedSieve te;
    private int lastProgress;
    private int lastEnergy;

    public ContainerAutomatedSieve(InventoryPlayer playerInventory, TileAutomatedSieve te) {
        this.te = te;
        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Слот 0: Вход (46, 32)
        this.addSlotToContainer(new SlotItemHandler(handler, 0, 46, 32));
        // Слот 1: Выход (111, 32) — запрет вставки
        this.addSlotToContainer(new SlotItemHandler(handler, 1, 111, 32) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        // Слот 2: Скорость (78, 6)
        this.addSlotToContainer(new SlotItemHandler(handler, 2, 78, 6));
        // Слот 3: Шанс (79, 57)
        this.addSlotToContainer(new SlotItemHandler(handler, 3, 79, 57));

        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                this.addSlotToContainer(new Slot(playerInventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (si = 0; si < 9; ++si)
            this.addSlotToContainer(new Slot(playerInventory, si, 8 + si * 18, 142));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getDistanceSq(te.getPos().getX() + 0.5D, te.getPos().getY() + 0.5D, te.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 4) {
                if (!this.mergeItemStack(itemstack1, 4, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack1.getItem() instanceof ItemSpeedUpgrade) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) return ItemStack.EMPTY;
                } else if (itemstack1.getItem() instanceof ItemChanceUpgrade) {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false)) return ItemStack.EMPTY;
                } else {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (lastProgress != te.progress || lastEnergy != te.energy.getEnergyStored()) {
            lastProgress = te.progress;
            lastEnergy = te.energy.getEnergyStored();
            for (net.minecraft.inventory.IContainerListener listener : this.listeners) {
                listener.sendWindowProperty(this, 0, te.progress);
                listener.sendWindowProperty(this, 1, te.energy.getEnergyStored());
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) te.progress = data;
        else if (id == 1) te.energy.setEnergyStored(data);
    }

    public int getProgress() {
        return te.progress;
    }

    public int getMaxProgress() {
        return te.maxProgress;
    }

    public int getEnergy() {
        return te.energy.getEnergyStored();
    }

    public int getMaxEnergy() {
        return te.energy.getMaxEnergyStored();
    }
}
