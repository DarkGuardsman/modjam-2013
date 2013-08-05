package dark.common.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.DarkBotMain;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.BlockMain;
import dark.common.prefab.BlockWrapper;
import dark.common.prefab.PosWorld;

public class BlockCreep extends BlockMain
{
    public static List<Block> ignoreList = new ArrayList<Block>();

    static
    {
        //TODO add special conversion cases like tree to metal tree
        //or water into acid pool
        ignoreList.add(DarkBotMain.blockCore);
        ignoreList.add(DarkBotMain.blockDeco);
        ignoreList.add(DarkBotMain.blockCreep);
        ignoreList.add(Block.blockIron);
        ignoreList.add(Block.blockGold);
        ignoreList.add(Block.blockDiamond);
        ignoreList.add(Block.oreCoal);
        ignoreList.add(Block.oreGold);
        ignoreList.add(Block.oreIron);
        ignoreList.add(Block.oreDiamond);
        ignoreList.add(Block.ladder);
        ignoreList.add(Block.redstoneWire);
        ignoreList.add(Block.lever);
    }

    public BlockCreep(int par1)
    {
        super(par1, "CreepingMetal", Material.iron);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setResistance(10000);
        this.setHardness(100);
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

                PosWorld pos = new PosWorld(world, i1, j1, k1);
                HiveSpire spire = HiveSpire.getSpire(pos, 100);
                /* Limit spread distance to around the tower */
                if (spire != null && spire.getLocation().getDistanceFrom2D(pos) < 300)
                {
                    PosWorld pos2 = new PosWorld(world, i1, j1 - 1, k1);
                    TileEntity entity = pos.getTileEntity();

                    int id = pos.getBlockID();
                    Block one = Block.blocksList[id];

                    int id2 = pos2.getBlockID();
                    Block two = Block.blocksList[id2];

                    if (entity == null && id != this.blockID && id2 != this.blockID && two != null && !ignoreList.contains(one))
                    {
                        world.setBlock(i1, j1, k1, this.blockID, meta, 3);
                        System.out.println("Creep spread to " + pos.toString());
                    }
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        PosWorld pos = new PosWorld(world, x, y, z);
        HiveSpire spire = HiveSpire.getSpire(pos, 100);
        /* Limit spread distance to around the tower */
        if (spire != null && spire.getLocation().getDistanceFrom(pos) < 300)
        {
            return true;
        }
        return false;
    }

    @Override
    public int idDropped(int par1, Random random, int par3)
    {
        if (random.nextInt(10) == 1)
        {
            return Item.ingotIron.itemID;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
        return this.blockIcon;
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
        super.registerIcons(par1IconRegister);
        this.blockIcon = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "MetalCreep");
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        PosWorld pos = new PosWorld(world, x, y, z);
        HiveSpire spire = HiveSpire.getSpire(pos, 100);
        if (spire != null)
        {
            spire.reportDeath(new BlockWrapper(this, pos));
        }
    }
}
