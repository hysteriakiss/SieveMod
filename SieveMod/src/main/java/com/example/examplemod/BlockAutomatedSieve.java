package com.example.examplemod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAutomatedSieve extends BlockContainer {

    public BlockAutomatedSieve() {
        super(Material.IRON);
        setUnlocalizedName("automated_sieve");
        setRegistryName("automated_sieve");
        setHardness(3.5F);
        setResistance(10.0F);
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("\u00A77" + "Automated Sieve Mod");
        tooltip.add("\u00A7e" + "Energy: RF");
        tooltip.add("\u00A7a" + "Processes blocks into resources");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileAutomatedSieve) {
            playerIn.openGui(SieveMod.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileAutomatedSieve) {
            TileAutomatedSieve sieve = (TileAutomatedSieve) tileentity;
            for (int i = 0; i < sieve.inventory.getSlots(); i++) {
                ItemStack stack = sieve.inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    net.minecraft.entity.item.EntityItem item = new net.minecraft.entity.item.EntityItem(
                            worldIn, pos.getX(), pos.getY(), pos.getZ(), stack.copy()
                    );
                    worldIn.spawnEntity(item);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAutomatedSieve();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
