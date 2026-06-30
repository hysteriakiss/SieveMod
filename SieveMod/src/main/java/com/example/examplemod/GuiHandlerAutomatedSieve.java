package com.example.examplemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerAutomatedSieve implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        net.minecraft.tileentity.TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileAutomatedSieve) {
            return new ContainerAutomatedSieve(player.inventory, (TileAutomatedSieve) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        net.minecraft.tileentity.TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileAutomatedSieve) {
            return new GuiAutomatedSieve(player.inventory, (TileAutomatedSieve) te);
        }
        return null;
    }
}
