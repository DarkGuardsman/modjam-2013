package dark.common.entity;

import dark.common.api.IHiveObject;
import dark.common.helpers.HiveManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityBot extends EntityLiving implements IHiveObject
{
    protected String hiveID = "world";

    public EntityBot(World par1World)
    {
        super(par1World);
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

}
