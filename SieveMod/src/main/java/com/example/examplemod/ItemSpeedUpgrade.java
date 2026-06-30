package com.example.examplemod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSpeedUpgrade extends Item {

    public ItemSpeedUpgrade() {
        setUnlocalizedName("speed_upgrade");
        setRegistryName("speed_upgrade");
        setMaxStackSize(4);
        setCreativeTab(CreativeTabs.MISC);
    }
}
