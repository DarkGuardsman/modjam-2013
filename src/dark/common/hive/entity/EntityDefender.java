package dark.common.hive.entity;

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

    public EntityDefender(World par1World)
    {
        super(par1World);
        this.experienceValue = 20;
    }

    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        float f = this.getBrightness(1.0F);

        if (f > 0.5F)
        {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    @Override
    public void setHiveID(String id)
    {
        this.hiveID = id;

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

    public EntityLiving getClosetEntityForAttack(double range)
    {
        EntityLiving entity = null;
        Pos pos = new Pos(this);
        double distance = range * range;

        List<EntityLiving> entityList = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, this.getBoundingBox().expand(range, 4, range));
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
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (super.attackEntityFrom(par1DamageSource, par2))
        {
            Entity entity = par1DamageSource.getEntity();

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
        else
        {
            return false;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        float f = (float) this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
        int i = 0;

        if (par1Entity instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) par1Entity);
            i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) par1Entity);
        }

        boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0)
            {
                par1Entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                par1Entity.setFire(j * 4);
            }

            if (par1Entity instanceof EntityLivingBase)
            {
                EnchantmentThorns.func_92096_a(this, (EntityLivingBase) par1Entity, this.rand);
            }
        }

        return flag;
    }

    @Override
    protected void attackEntity(Entity par1Entity, float par2)
    {
        //TODO add ranged attack
        if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
        {
            this.attackTime = 20;
            this.attackEntityAsMob(par1Entity);
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

}
