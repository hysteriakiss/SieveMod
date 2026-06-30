package com.example.examplemod;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SiftingRecipeWrapper implements IRecipeWrapper {

    private final ItemStack input;
    private final List<ItemStack> outputs;
    private final List<Float> chances;

    public SiftingRecipeWrapper(SiftingRecipe recipe) {
        this.input = new ItemStack(recipe.getInputBlock());
        this.outputs = new ArrayList<>();
        this.chances = new ArrayList<>();

        for (SiftingRecipe.RecipeOutput output : recipe.getOutputs()) {
            this.outputs.add(output.item.copy());
            this.chances.add(output.chance);
        }
    }

    public ItemStack getInput() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public List<Float> getChances() {
        return chances;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
    }
}
