package com.example.examplemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CraftingHandler {

    private static final String SIEVE_BLOCK = "examplemod:automated_sieve";

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        IInventory craftMatrix = event.craftMatrix;
        ItemStack result = event.crafting;

        boolean usesSieveBlock = false;

        for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
            ItemStack stack = craftMatrix.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem().getRegistryName() != null
                    && stack.getItem().getRegistryName().toString().equals(SIEVE_BLOCK)) {
                usesSieveBlock = true;
                break;
            }
        }

        if (!usesSieveBlock) {
            return;
        }

        if (result.isEmpty()) {
            return;
        }

        String resultName = result.getItem().getRegistryName() != null
                ? result.getItem().getRegistryName().toString()
                : "";

        if (resultName.equals(SIEVE_BLOCK)) {
            return;
        }

        if (resultName.startsWith("minecraft:")) {
            for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
                ItemStack stack = craftMatrix.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem().getRegistryName() != null
                        && stack.getItem().getRegistryName().toString().equals(SIEVE_BLOCK)) {
                    if (!player.world.isRemote) {
                        if (!player.inventory.addItemStackToInventory(stack.copy())) {
                            player.entityDropItem(stack.copy(), 0);
                        }
                    }
                    craftMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }
    }
}
