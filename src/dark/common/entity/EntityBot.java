package dark.common.entity;

import dark.common.api.IHiveObject;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityBot extends EntityLiving implements IHiveObject
{
    protected int hiveID = -1;
    public EntityBot(World par1World)
    {
        super(par1World);
    }

    @Override
    public void setHiveID(int id)
    {
        this.hiveID = id;

    }
    @Override
    public int getHiveID()
    {
       if(this.hiveID == -1)
       {

       }
        return 0;
    }

}
