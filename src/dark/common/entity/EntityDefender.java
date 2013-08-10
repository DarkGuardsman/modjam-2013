package dark.common.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import dark.common.api.IHiveObject;

public class EntityDefender extends EntityWeaponDrone
{

    public EntityDefender(World par1World)
    {
        super(par1World);
        this.isImmuneToFire = true;
        this.experienceValue = 30;
        this.setSize(1.5f, 2);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }



    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        float damage = (float) this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
        int knockBack = 0;

        if (entity instanceof EntityLivingBase)
        {
            damage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) entity);
            knockBack += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) entity);
        }

        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);

        if (flag)
        {
            if (knockBack > 0)
            {
                entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) knockBack * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) knockBack * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entity.setFire(j * 4);
            }

            if (entity instanceof EntityLivingBase)
            {
                EnchantmentThorns.func_92096_a(this, (EntityLivingBase) entity, this.rand);
            }
        }

        return flag;
    }

    public void rangedAttack(Entity attackTarget, float range)
    {
        double deltaX = attackTarget.posX - this.posX;
        double deltaY = attackTarget.boundingBox.minY + (double) (attackTarget.height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
        double deltaZ = attackTarget.posZ - this.posZ;

        if (this.attackTime == 0)
        {
            this.attackTime = 15;
            this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) this.posX, (int) this.posY, (int) this.posZ, 0);

            for (int i = 0; i < 1; ++i)
            {
                EntityBombMissile entitysmallfireball = new EntityBombMissile(this.worldObj, this, (EntityLivingBase) attackTarget, 1.6F, (float) (14 - this.worldObj.difficultySetting * 4));
                entitysmallfireball.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
                entitysmallfireball.setDamage((double) (range * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting * 0.11F));
                this.worldObj.spawnEntityInWorld(entitysmallfireball);
            }
        }

        this.rotationYaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
        this.hasAttacked = true;

    }

    @Override
    protected void attackEntity(Entity attackTarget, float range)
    {
        if (attackTarget instanceof EntityLivingBase)
        {
            if (this.attackTime <= 0 && range < 2.0F && attackTarget.boundingBox.maxY > this.boundingBox.minY && attackTarget.boundingBox.minY < this.boundingBox.maxY)
            {
                this.attackTime = 20;
                this.attackEntityAsMob(attackTarget);
            }
            else if (range < 30.0F)
            {
                this.rangedAttack(attackTarget, range);
            }

        }
    }

    @Override
    protected void func_110147_ax()
    {
        super.func_110147_ax();
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e);
    }

    public boolean func_70845_n()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void func_70844_e(boolean par1)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            b0 = (byte) (b0 | 1);
        }
        else
        {
            b0 &= -2;
        }

        this.dataWatcher.updateObject(16, Byte.valueOf(b0));
    }

}
