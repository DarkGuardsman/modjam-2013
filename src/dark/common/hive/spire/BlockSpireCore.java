package dark.common.hive.spire;

import net.minecraft.block.material.Material;
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
    public void breakBlock(World world, int x, int par3, int par4, int par5, int par6)
    {
        super.breakBlock(world, x, par3, par4, par5, par6);
    }

}
