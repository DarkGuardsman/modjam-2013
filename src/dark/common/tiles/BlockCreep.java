package dark.common.tiles;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.DarkBotMain;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.BlockMain;
import dark.common.prefab.BlockWrapper;
import dark.common.prefab.PosWorld;

public class BlockCreep extends BlockMain
{

    public BlockCreep(int par1)
    {
        super(par1, "CreepingMetal", Material.iron);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockBounds(0, 0, 0, 1f, .2f, 1f);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        AxisAlignedBB newBounds = AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 0.2, z + 1);

        if (newBounds != null && par5AxisAlignedBB.intersectsWith(newBounds))
        {
            par6List.add(newBounds);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote)
        {
            int meta = world.getBlockMetadata(x, y, z);
            for (int l = 0; l < 4; ++l)
            {
                int i1 = x + random.nextInt(3) - 1;
                int j1 = y + random.nextInt(5) - 3;
                int k1 = z + random.nextInt(3) - 1;
                int blockID = world.getBlockId(i1, j1, k1);
                int blockIDB = world.getBlockId(i1, j1 - 1, k1);
                if (blockID == 0 && blockIDB != 0 && blockIDB != this.blockID)
                {
                    world.setBlock(i1, j1, k1, this.blockID, meta, 3);
                }
            }
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return true;
    }

    @Override
    public int idDropped(int par1, Random random, int par3)
    {
        if (random.nextInt(10) == 1)
        {
            if (OreDictionary.getOres("copperWire").size() > 0)
            {
                ItemStack stack = OreDictionary.getOres("copperWire").get(0);
                return stack != null ? stack.itemID : 0;
            }
            else
            {
                return Item.ingotIron.itemID;
            }
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
        if (side == 1)
        {
            return this.blockIcon;
        }
        else
        {
            return Block.blockIron.getIcon(side, 0);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta)
    {
        return side == 1 ? this.blockIcon : (side == 0 ? Block.dirt.getBlockTextureFromSide(side) : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "wireCreep");
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        PosWorld pos = new PosWorld(world,x,y,z);
       HiveSpire spire =  HiveSpire.getSpire(pos, 100);
       if(spire != null)
       {
           spire.reportDeath(new BlockWrapper(this, pos));
       }
    }
}
