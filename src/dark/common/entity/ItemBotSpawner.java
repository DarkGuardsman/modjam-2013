package dark.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBotSpawner extends Item
{
    @SideOnly(Side.CLIENT)
    private Icon theIcon;

    public ItemBotSpawner(int par1)
    {
        super(par1);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (itemStack != null)
        {
            if (itemStack.getItemDamage() == 0)
            {
                par3List.add("Spire defensive drone");
            }
            if (itemStack.getItemDamage() == 1)
            {
                par3List.add("Core Boss Drone");
            }
        }
    }

    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItemDamage() == 0)
        {
            return "Defender II";
        }
        if (par1ItemStack.getItemDamage() == 1)
        {
            return "Core Guardian";
        }
        return "Unkown Item";
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            int i1 = world.getBlockId(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d0 = 0.0D;

            if (side == 1 && Block.blocksList[i1] != null && Block.blocksList[i1].getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            this.spawnCreature(world, (double) x + 0.5D, (double) y + d0, (double) z + 0.5D, itemstack.getItemDamage());

            return true;
        }
    }

    /** Spawns the creature specified by the egg's type in the location specified by the last three
     * parameters. Parameters: world, entityID, x, y, z. */
    public void spawnCreature(World world, double x, double y, double z, int i)
    {
        EntityLiving entityliving = null;
        if (i == 0)
        {
            entityliving = new EntityDefender(world);
        }
        if (i == 1)
        {
            entityliving = new EntityBossGigus(world);
        }

        if (entityliving != null)
        {
            entityliving.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            world.spawnEntityInWorld(entityliving);
            entityliving.playLivingSound();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
    }
}
