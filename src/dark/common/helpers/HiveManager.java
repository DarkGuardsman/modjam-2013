package dark.common.helpers;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dark.common.prefab.Pos;

public class HiveManager
{

    protected Set<NetworkHivemind> hives = new HashSet<NetworkHivemind>();
    public static final String NEUTRIAL = "NEUT";

    public void registerHive(NetworkHivemind mind)
    {
        if (!this.hives.contains(mind))
        {
            hives.add(mind);
        }
    }

    /** Gets the string ID the bot or Tile will use to ID itself as part of the hive */
    public String getHiveID(Object obj)
    {
        NetworkHivemind hive = null;
        double distance = Double.MAX_VALUE;
        Pos pos = null;
        World world = null;

        if (obj instanceof Entity)
        {
            pos = new Pos((Entity) obj);
            world = ((Entity) obj).worldObj;
        }
        else if (obj instanceof TileEntity)
        {
            pos = new Pos((TileEntity) obj);
            world = ((TileEntity) obj).worldObj;
        }

        if (pos != null && world != null)
        {
            for (NetworkHivemind entry : hives)
            {
                double distanceTo = entry.getLocation().getDistanceFrom(pos);
                if (distanceTo < distance)
                {
                    hive = entry;
                    distance = distanceTo;
                }
            }
            if (hive != null)
            {
                return hive.getID();
            }
        }

        return NEUTRIAL;
    }
}
