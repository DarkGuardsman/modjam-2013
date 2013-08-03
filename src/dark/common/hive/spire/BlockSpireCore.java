package dark.common.hive.spire;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import dark.client.renders.RenderCore;
import dark.common.prefab.BlockMain;
import dark.common.prefab.Pos;

public class BlockSpireCore extends BlockMain
{

    public BlockSpireCore(int id)
    {
        super(id, "SpireCore", Material.iron);
        this.setHardness(1000);
        this.setResistance(100000);
        this.setCreativeTab(CreativeTabs.tabBlock);

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking())
        {
            float change = 0.1f;
            ItemStack stack = player.getHeldItem();
            if(stack != null)
            {
                if(stack.getItem().itemID == Item.stick.itemID)
                {
                    change = 0.01f;
                }
            }
            Pos pos = new Pos();
            pos.modifyBy(ForgeDirection.getOrientation(side));
            RenderCore.xChange += (float) pos.xx * change;
            RenderCore.yChange += (float) pos.yy * change;
            RenderCore.zChange += (float) pos.zz * change;
            System.out.println(RenderCore.xChange + "x " + RenderCore.yChange + "y " + RenderCore.zChange + "z ");
        }
        else
        {
            RenderCore.xChange = 0;
            RenderCore.yChange = 0;
            RenderCore.zChange = 0;
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if (entity instanceof TileEntitySpire)
        {

        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata == 0)
        {
            return new TileEntitySpire();
        }
        else
        {
            return null;
        }
    }

}
