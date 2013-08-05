package dark.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import dark.common.DarkBotMain;
import dark.common.api.IHiveObject;
import dark.common.hive.HiveManager;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class EntityDefender extends EntityCreature implements IHiveObject
{
    private String hiveID = "world";
    private HiveSpire spire = null;
    private boolean reported = false;

    public EntityDefender(World par1World)
    {
        super(par1World);
        this.isImmuneToFire = true;
        this.experienceValue = 30;
        this.setSize(1.5f, 2);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        super.onLivingUpdate();
        if (!this.worldObj.isRemote && !reported)
        {
            if (this.getSpire() != null)
            {
                this.getSpire().reportIn(this);
            }
        }
    }

    @Override
    public void setHiveID(String id)
    {
        this.hiveID = id;

    }

    @Override
    public void setDead()
    {
        super.setDead();
        if (this.getSpire() != null)
        {
            this.getSpire().reportDeath(this);
        }
    }

    @Override
    public String getHiveID()
    {
        if (this.hiveID.equalsIgnoreCase("world") || this.hiveID.equalsIgnoreCase(HiveManager.NEUTRIAL))
        {
            if (this.getSpire() != null)
            {
                this.hiveID = this.getSpire().getHiveID();
            }
            else
            {
                this.hiveID = HiveManager.getHiveID(this);
            }
        }
        return this.hiveID;
    }

    public HiveSpire getSpire()
    {
        if (spire == null)
        {
            spire = HiveSpire.getSpire(new PosWorld(this.worldObj, new Pos(this)), 3);
        }
        return spire;
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        return this.getClosetEntityForAttack(20);
    }

    /** Gets the closest entity to this entity for attack */
    public EntityLiving getClosetEntityForAttack(double range)
    {
        EntityLiving entity = null;
        Pos pos = new Pos(this);
        double distance = range * range;
        this.getBoundingBox();
        List<EntityLiving> entityList = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, this.boundingBox.expand(range, 4, range));
        for (EntityLiving currentEntity : entityList)
        {
            if (currentEntity instanceof IHiveObject && ((IHiveObject) currentEntity).getHiveID().equalsIgnoreCase(this.getHiveID()))
            {

            }
            else if (this.canEntityBeSeen(currentEntity) && !currentEntity.isInvisible() && currentEntity.isEntityAlive())
            {
                double distanceTo = pos.getDistanceFrom(new Pos(currentEntity));
                if (distanceTo < distance)
                {
                    distance = distanceTo;
                    entity = currentEntity;
                }
            }
        }

        return entity;
    }

    @Override
    // called when this is attacked
    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (source != null)
        {
            Entity entity = source.getEntity();
            if (entity != null && entity instanceof IHiveObject && ((IHiveObject) entity).getHiveID().equalsIgnoreCase(this.getHiveID()))
            {
                return false;
            }
            else if (super.attackEntityFrom(source, damage))
            {
                if (this.riddenByEntity != entity && this.ridingEntity != entity)
                {
                    if (entity != this)
                    {
                        this.entityToAttack = entity;
                    }

                    return true;
                }
                else
                {
                    return true;
                }
            }
        }
        return false;
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
            this.attackTime = 20;
            this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, (int) this.posX, (int) this.posY, (int) this.posZ, 0);

            for (int i = 0; i < 1; ++i)
            {
                EntityProj entitysmallfireball = new EntityProj(this.worldObj, this, (EntityLivingBase) attackTarget, 1.6F, (float) (14 - this.worldObj.difficultySetting * 4));
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
    public float getBlockPathWeight(int x, int y, int z)
    {
        PosWorld pos = new PosWorld(this.worldObj, x, y, z);
        int blockID = pos.getBlockID();
        Block block = Block.blocksList[blockID];
        if (blockID == DarkBotMain.blockCreep.blockID || blockID == DarkBotMain.blockDeco.blockID)
        {
            return 100;
        }
        else if (block != null)
        {
            if (block instanceof BlockFluid || block instanceof IFluidBlock)
            {
                return -1000;
            }
        }
        return 0.5F + this.worldObj.getLightBrightness(x, y, z);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return this.getSpire() != null && this.getSpire().getLocation().getDistanceFrom2D(new Pos(this)) < 200;
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
