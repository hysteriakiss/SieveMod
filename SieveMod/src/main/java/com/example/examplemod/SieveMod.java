package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = SieveMod.MODID, name = SieveMod.NAME, version = SieveMod.VERSION)
@Mod.EventBusSubscriber
public class SieveMod {
    public static final String MODID = "examplemod";
    public static final String NAME = "Automated Sieve Mod";
    public static final String VERSION = "1.0";

    public static Logger logger;
    public static BlockAutomatedSieve automatedSieve = new BlockAutomatedSieve();
    public static ItemBlock automatedSieveItem = new ItemBlock(automatedSieve);
    public static ItemSpeedUpgrade speedUpgrade = new ItemSpeedUpgrade();
    public static ItemChanceUpgrade chanceUpgrade = new ItemChanceUpgrade();

    @Mod.Instance
    public static SieveMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        instance = this;
        GameRegistry.registerTileEntity(TileAutomatedSieve.class, MODID + ":automated_sieve_tile");
        SiftingRecipeRegistry.registerRecipes();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new CraftingHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerAutomatedSieve());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(automatedSieve);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        automatedSieveItem.setRegistryName(automatedSieve.getRegistryName());
        event.getRegistry().register(automatedSieveItem);
        event.getRegistry().register(speedUpgrade);
        event.getRegistry().register(chanceUpgrade);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(automatedSieveItem, 0,
                new net.minecraft.client.renderer.block.model.ModelResourceLocation(automatedSieveItem.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(speedUpgrade, 0,
                new net.minecraft.client.renderer.block.model.ModelResourceLocation(speedUpgrade.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(chanceUpgrade, 0,
                new net.minecraft.client.renderer.block.model.ModelResourceLocation(chanceUpgrade.getRegistryName(), "inventory"));
    }
}
