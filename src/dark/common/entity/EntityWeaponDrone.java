package dark.common.entity;

import java.util.List;

import dark.common.api.IHiveObject;
import dark.common.prefab.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityWeaponDrone extends EntityHiveDrone
{

    public EntityWeaponDrone(World par1World)
    {
        super(par1World);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        return this.getClosetEntityForAttack(30);
    }

    @SuppressWarnings("unchecked")
    public EntityLivingBase getClosetEntityForAttack(double range)
    {
        EntityLivingBase entity = null;
        Pos pos = new Pos(this);
        double distance = range * range;
        this.getBoundingBox();
        List<EntityLivingBase> entityList = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(range, 4, range));

        for (EntityLivingBase currentEntity : entityList)
        {
            if (currentEntity instanceof EntityPlayer && ((EntityPlayer) currentEntity).capabilities.isCreativeMode)
            {

            }
            else if (currentEntity instanceof IHiveObject && ((IHiveObject) currentEntity).getHiveID().equalsIgnoreCase(this.getHiveID()))
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

}
