package dark.common.entity;

import dark.common.DarkBotMain;
import dark.common.api.IHiveEntity;
import dark.common.api.IHiveObject;
import dark.common.hive.HiveManager;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.PosWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFluid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

public class EntityHiveDrone extends EntityCreature implements IHiveEntity
{
    protected String hiveID = "world";
    protected HiveSpire spire = null;
    private boolean reported = false;

    public EntityHiveDrone(World par1World)
    {
        super(par1World);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        super.onLivingUpdate();
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
                /*Need to prevent friendly fire and friendly aggro*/
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
    public void setHiveID(String id)
    {
        this.hiveID = id;

    }

    @Override
    public String getHiveID()
    {
        if (this.hiveID.equalsIgnoreCase("world") || this.hiveID.equalsIgnoreCase(HiveManager.NEUTRIAL))
        {
            this.hiveID = HiveManager.getHiveID(this);
        }
        return this.hiveID;
    }

    @Override
    public void addPotionEffect(PotionEffect par1PotionEffect)
    {
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
            if (block instanceof BlockFluid || block instanceof IFluidBlock || block instanceof BlockFire)
            {
                return -1000;
            }
        }
        return 0.5F + this.worldObj.getLightBrightness(x, y, z);
    }

}
