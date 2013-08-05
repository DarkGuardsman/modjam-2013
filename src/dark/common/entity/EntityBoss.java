package dark.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.hive.entity.EntityDefender;

public class EntityBoss extends EntityDefender implements IBossDisplayData
{
    private float[] field_82220_d = new float[2];
    private float[] field_82221_e = new float[2];
    private int field_82222_j;

    public EntityBoss(World par1World)
    {
        super(par1World);
        this.setEntityHealth(100 + 100 * this.worldObj.difficultySetting);
        this.setSize(0.9F, 4.0F);
        this.isImmuneToFire = true;
        this.getNavigator().setCanSwim(true);
        this.experienceValue = 200;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound bt)
    {
        super.writeEntityToNBT(bt);
        bt.setInteger("Invul", this.getInvulCounter());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.setInvulCounter(nbt.getInteger("Invul"));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return this.height / 8.0F;
    }

    /** Returns the sound this mob makes while it's alive. */
    protected String getLivingSound()
    {
        return "mob.wither.idle";
    }

    /** Returns the sound this mob makes when it is hurt. */
    protected String getHurtSound()
    {
        return "mob.wither.hurt";
    }

    /** Returns the sound this mob makes on death. */
    protected String getDeathSound()
    {
        return "mob.wither.death";
    }

    /** Sets the Entity inside a web block. */
    public void setInWeb()
    {
    }

    /** Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue */
    public int getTotalArmorValue()
    {
        return 4;
    }

    private double func_82214_u(int par1)
    {
        if (par1 <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + (float) (180 * (par1 - 1))) / 180.0F * (float) Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + (double) f1 * 1.3D;
        }
    }

    private double func_82208_v(int par1)
    {
        return par1 <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double func_82213_w(int par1)
    {
        if (par1 <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + (float) (180 * (par1 - 1))) / 180.0F * (float) Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double) f1 * 1.3D;
        }
    }

    private float func_82204_b(float par1, float par2, float par3)
    {
        float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);

        if (f3 > par3)
        {
            f3 = par3;
        }

        if (f3 < -par3)
        {
            f3 = -par3;
        }

        return par1 + f3;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (source == DamageSource.drown)
        {
            return false;
        }
        else if (this.getInvulCounter() > 0)
        {
            return false;
        }
        else
        {
            Entity entity;

            if (this.isArmored())
            {
                entity = source.getSourceOfDamage();

                if (entity instanceof EntityArrow)
                {
                    return false;
                }
            }

            entity = source.getEntity();

            if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getCreatureAttribute() == this.getCreatureAttribute())
            {
                return false;
            }
            else
            {
                if (this.field_82222_j <= 0)
                {
                    this.field_82222_j = 20;
                }

                for (int i = 0; i < this.field_82224_i.length; ++i)
                {
                    this.field_82224_i[i] += 3;
                }

                return super.attackEntityFrom(source, damage);
            }
        }
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
        for (int i = 0; i < this.worldObj.rand.nextInt(100); i++)
        {
            this.dropItem(Item.ingotIron.itemID, 1);
        }
    }

    @Override
    protected void despawnEntity()
    {
        this.entityAge = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    public void addPotionEffect(PotionEffect par1PotionEffect)
    {
    }

    protected void func_110147_ax()
    {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(300.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.6000000238418579D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(40.0D);
    }

    @SideOnly(Side.CLIENT)
    public float func_82207_a(int par1)
    {
        return this.field_82221_e[par1];
    }

    @SideOnly(Side.CLIENT)
    public float func_82210_r(int par1)
    {
        return this.field_82220_d[par1];
    }

    public int getInvulCounter()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    public void setInvulCounter(int par1)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(par1));
    }

    /** Returns the target entity ID if present, or -1 if not @param par1 The target offset, should
     * be from 0-2 */
    public int getWatchedTargetId(int par1)
    {
        return this.dataWatcher.getWatchableObjectInt(17 + par1);
    }

    public void func_82211_c(int par1, int par2)
    {
        this.dataWatcher.updateObject(17 + par1, Integer.valueOf(par2));
    }

    /** Called when a player mounts an entity. e.g. mounts a pig, mounts a boat. */
    public void mountEntity(Entity par1Entity)
    {
        this.ridingEntity = null;
    }
}
