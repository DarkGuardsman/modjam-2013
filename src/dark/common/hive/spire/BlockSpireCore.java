package dark.common.hive.spire;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dark.common.prefab.BlockMain;

public class BlockSpireCore extends BlockMain
{

    public BlockSpireCore(int id)
    {
        super(id, "SpireCore", Material.iron);
        this.setHardness(1000);
        this.setResistance(100000);
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

}
