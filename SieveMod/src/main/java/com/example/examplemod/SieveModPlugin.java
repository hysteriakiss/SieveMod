package com.example.examplemod;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class SieveModPlugin implements IModPlugin {

    public static final String CATEGORY_SIFTING = "examplemod:sifting";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new SiftingCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(SiftingRecipe.class, SiftingRecipeWrapper::new, CATEGORY_SIFTING);

        List<SiftingRecipeWrapper> wrappers = new ArrayList<>();
        for (SiftingRecipe recipe : SiftingRecipeRegistry.getAllRecipes()) {
            wrappers.add(new SiftingRecipeWrapper(recipe));
        }
        registry.addRecipes(wrappers, CATEGORY_SIFTING);
    }
}
