package dark.common.prefab;

import dark.common.DarkBotMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockMain extends Block
{
    protected Icon machineSide;

    public BlockMain(int id, String name, Material material)
    {
        super(DarkBotMain.config.getBlock(name, id).getInt(), material);
        this.setUnlocalizedName(name);
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return this.blockIcon;
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        machineSide = par1IconRegister.registerIcon(DarkBotMain.PREFIX+"machineSide");
    }

    @Override
    public int damageDropped(int meta)
    {
        return 0;
    }

    @Override
    public float getExplosionResistance(Entity entity)
    {
        return this.blockResistance / 5.0F;
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5)
    {
        //TODO if hive block trigger hive aggresion
    }

    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        //TODO inform spire of entity if its not of the hive
    }

    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
    }

    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return true;
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
        //TODO drop inv
    }

}
