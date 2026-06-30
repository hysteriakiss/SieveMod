package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SiftingRecipeRegistry {
    private static final Map<Block, SiftingRecipe> recipes = new HashMap<>();

    public static void registerRecipes() {
        SiftingRecipe dirtRecipe = new SiftingRecipe(Blocks.DIRT);
        dirtRecipe.addOutput(new ItemStack(Items.DIAMOND, 1), 0.05f);
        dirtRecipe.addOutput(new ItemStack(Items.IRON_INGOT, 1), 0.15f);
        dirtRecipe.addOutput(new ItemStack(Items.GOLD_NUGGET, 2), 0.25f);
        dirtRecipe.addOutput(new ItemStack(Items.COAL, 1), 0.45f);
        recipes.put(Blocks.DIRT, dirtRecipe);

        SiftingRecipe gravelRecipe = new SiftingRecipe(Blocks.GRAVEL);
        gravelRecipe.addOutput(new ItemStack(Items.FLINT, 1), 0.40f);
        gravelRecipe.addOutput(new ItemStack(Items.IRON_NUGGET, 2), 0.25f);
        gravelRecipe.addOutput(new ItemStack(Items.GOLD_NUGGET, 1), 0.15f);
        gravelRecipe.addOutput(new ItemStack(Items.COAL, 1), 0.15f);
        recipes.put(Blocks.GRAVEL, gravelRecipe);

        SiftingRecipe sandRecipe = new SiftingRecipe(Blocks.SAND);
        sandRecipe.addOutput(new ItemStack(Items.DYE, 2, 4), 0.35f);
        sandRecipe.addOutput(new ItemStack(Items.BONE, 1), 0.25f);
        sandRecipe.addOutput(new ItemStack(Items.GOLD_NUGGET, 1), 0.10f);
        sandRecipe.addOutput(new ItemStack(Items.CLAY_BALL, 2), 0.20f);
        recipes.put(Blocks.SAND, sandRecipe);

        SiftingRecipe soulSandRecipe = new SiftingRecipe(Blocks.SOUL_SAND);
        soulSandRecipe.addOutput(new ItemStack(Items.QUARTZ, 2), 0.35f);
        soulSandRecipe.addOutput(new ItemStack(Items.GHAST_TEAR, 1), 0.10f);
        soulSandRecipe.addOutput(new ItemStack(Items.BLAZE_POWDER, 1), 0.20f);
        soulSandRecipe.addOutput(new ItemStack(Items.NETHER_WART, 1), 0.25f);
        recipes.put(Blocks.SOUL_SAND, soulSandRecipe);
    }

    public static SiftingRecipe getRecipe(Block inputBlock) {
        return recipes.get(inputBlock);
    }

    public static boolean hasRecipe(Block inputBlock) {
        return recipes.containsKey(inputBlock);
    }

    public static java.util.Collection<SiftingRecipe> getAllRecipes() {
        return recipes.values();
    }
}
