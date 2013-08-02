package dark.common.tiles;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import dark.common.prefab.BlockMain;

public class BlockDecor extends BlockMain
{
    public BlockDecor(int id)
    {
        super(id, "SpireWall", Material.rock);
        this.setHardness(1000);
        this.setResistance(100000);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
    }

}
