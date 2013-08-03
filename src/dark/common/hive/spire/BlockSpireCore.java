package dark.common.hive.spire;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
            Pos pos = new Pos();
            pos.modifyBy(ForgeDirection.getOrientation(side));
            pos.multi(.1);
            RenderCore.xChange += (float) pos.xx;
            RenderCore.yChange += (float) pos.yy;
            RenderCore.zChange += (float) pos.zz;
            System.out.println(RenderCore.xChange + "x " + RenderCore.yChange + "y " + RenderCore.zChange + "z ");
        }
        else
        {

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
