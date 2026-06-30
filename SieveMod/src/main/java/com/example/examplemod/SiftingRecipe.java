package com.example.examplemod;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SiftingRecipe {
    private final Block inputBlock;
    private final List<RecipeOutput> outputs;

    public SiftingRecipe(Block inputBlock) {
        this.inputBlock = inputBlock;
        this.outputs = new ArrayList<>();
    }

    public void addOutput(ItemStack output, float chance) {
        this.outputs.add(new RecipeOutput(output, chance));
    }

    public Block getInputBlock() {
        return inputBlock;
    }

    public List<RecipeOutput> getOutputs() {
        return outputs;
    }

    public ItemStack getRandomOutput(Random rand, double luckModifier) {
        float totalChance = 0.0f;
        for (RecipeOutput entry : outputs) {
            totalChance += entry.chance;
        }

        float r = rand.nextFloat() * totalChance;
        float currentChance = 0.0f;

        for (RecipeOutput entry : outputs) {
            currentChance += entry.chance;
            if (r <= currentChance) {
                ItemStack output = entry.item.copy();
                int quantity = output.getCount();
                if (luckModifier > 1.0) {
                    quantity = (int) Math.round(quantity * luckModifier);
                    output.setCount(quantity);
                }
                return output;
            }
        }
        return ItemStack.EMPTY;
    }

    public static class RecipeOutput {
        final ItemStack item;
        final float chance;

        RecipeOutput(ItemStack item, float chance) {
            this.item = item;
            this.chance = chance;
        }
    }
}
