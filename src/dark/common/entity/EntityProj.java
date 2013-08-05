package dark.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityProj extends EntityArrow
{
    public EntityProj(World par1World)
    {
        super(par1World);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntityProj(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par5, float par7)
    {
        super(par1World, par2EntityLivingBase, par3EntityLivingBase, par5, par7);
    }

    public EntityProj(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }


    protected void onImpact(MovingObjectPosition vec)
    {
        if (!this.worldObj.isRemote)
        {
            this.worldObj.createExplosion(this, vec.blockX, vec.blockY, vec.blockZ, 5, false);
            this.setDead();
        }
    }

    /** Returns true if other Entities should be prevented from moving through this Entity. */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /** Called when the entity is attacked. */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        return false;
    }
}
