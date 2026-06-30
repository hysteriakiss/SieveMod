package com.example.examplemod;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SiftingCategory implements IRecipeCategory<SiftingRecipeWrapper> {

    private final IDrawable background;

    public SiftingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
                new net.minecraft.util.ResourceLocation("textures/gui/container/generic_54.png"),
                0, 0, 166, 54);
    }

    @Override
    public String getUid() {
        return SieveModPlugin.CATEGORY_SIFTING;
    }

    @Override
    public String getTitle() {
        return "Sifting";
    }

    @Override
    public String getModName() {
        return "SieveMod";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SiftingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 2, 2);
        guiItemStacks.set(0, recipeWrapper.getInput());

        List<ItemStack> outputs = recipeWrapper.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            guiItemStacks.init(i + 1, false, 40 + i * 28, 2);
            guiItemStacks.set(i + 1, outputs.get(i));
        }
    }
}
