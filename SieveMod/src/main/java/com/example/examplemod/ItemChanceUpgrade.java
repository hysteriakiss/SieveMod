package com.example.examplemod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemChanceUpgrade extends Item {

    public ItemChanceUpgrade() {
        setUnlocalizedName("chance_upgrade");
        setRegistryName("chance_upgrade");
        setMaxStackSize(4);
        setCreativeTab(CreativeTabs.MISC);
    }
}
