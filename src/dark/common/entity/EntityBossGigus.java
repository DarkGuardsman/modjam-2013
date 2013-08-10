package dark.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityBossGigus extends EntityDefender implements IBossDisplayData
{

    public EntityBossGigus(World par1World)
    {
        super(par1World);
        this.setEntityHealth(100 + 100 * this.worldObj.difficultySetting);
        this.isImmuneToFire = true;
        this.getNavigator().setCanSwim(true);
        this.experienceValue = 200;
        this.setSize(10, 10);
        this.jumpMovementFactor = 4;
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

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (this.worldObj.isRemote)
        {
            this.worldObj.spawnParticle("mobSpell", this.posX, this.posY, this.posZ, 0, 0, 0);

        }
    }

    @Override
    public String getEntityName()
    {
        return "Core Guardian";
    }

    @Override
    public void rangedAttack(Entity attackTarget, float range)
    {
        double deltaX = attackTarget.posX - this.posX;
        double deltaY = attackTarget.boundingBox.minY + (double) (attackTarget.height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
        double deltaZ = attackTarget.posZ - this.posZ;

        if (this.attackTime == 0)
        {
            this.attackTime = 200;
            this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) this.posX, (int) this.posY, (int) this.posZ, 0);

            for (int i = 0; i < 12; ++i)
            {
                EntityBombMissile entitysmallfireball = new EntityBombMissile(this.worldObj, this, (EntityLivingBase) attackTarget, 1.6F, (float) (14 - this.worldObj.difficultySetting * 4));
                entitysmallfireball.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
                entitysmallfireball.setDamage((double) (range * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting * 0.11F));
                this.worldObj.spawnEntityInWorld(entitysmallfireball);
            }
        }
        else if (this.attackTime % 10 == 0)
        {
            for (int i = 0; i < 3; ++i)
            {
                EntityArrow entitysmallfireball = new EntityArrow(this.worldObj, this, (EntityLivingBase) attackTarget, 1.6F, (float) (14 - this.worldObj.difficultySetting * 4));
                entitysmallfireball.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
                entitysmallfireball.setDamage((double) (range * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.difficultySetting * 0.11F));
                this.worldObj.spawnEntityInWorld(entitysmallfireball);
            }

        }

        this.rotationYaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
        this.hasAttacked = true;

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
        return "mods.DarkBots.Fire";
    }

    /** Sets the Entity inside a web block. */
    public void setInWeb()
    {
    }

    @Override
    public int getTotalArmorValue()
    {
        return 4 + this.worldObj.difficultySetting;
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

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    protected void func_110147_ax()
    {
        super.func_110147_ax();

        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(12.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(300.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.6000000238418582D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(40.0D);
    }

    public int getInvulCounter()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    public void setInvulCounter(int par1)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(par1));
    }

    @Override
    public void mountEntity(Entity par1Entity)
    {
        this.ridingEntity = null;
    }
}
