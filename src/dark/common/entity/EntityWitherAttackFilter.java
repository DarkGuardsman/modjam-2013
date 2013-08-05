package dark.common.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import dark.common.api.IHiveObject;

final class EntityWitherAttackFilter implements IEntitySelector
{
    /** Return whether the specified entity is applicable to this filter. */
    public boolean isEntityApplicable(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            if (!(entity instanceof IHiveObject) && !entity.isDead)
            {
                return true;
            }
        }
        return false;
    }
}
