package dark.common.tiles;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import dark.common.DarkBotMain;
import dark.common.prefab.BlockMain;

public class BlockDecor extends BlockMain
{
    public static Icon[] icons = new Icon[16];

    public BlockDecor(int id)
    {
        super(id, "SpireWall", Material.rock);
        this.setHardness(10);
        this.setResistance(100000);
        this.setCreativeTab(CreativeTabs.tabBlock);

    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        int id = world.getBlockId(x, y, z);
        int m = world.getBlockMetadata(x, y, z);
        if (id == this.blockID && m == 1)
        {
            return 10;
        }
        return super.getLightValue(world, x, y, z);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        icons[0] = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "blackMachine");
        icons[1] = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "redMachine");
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        if (icons[meta] != null)
        {
            return icons[meta];
        }
        return this.machineSide;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List list)
    {
        list.add(new ItemStack(this.blockID, 1, 0));
        list.add(new ItemStack(this.blockID, 1, 1));
    }

}
