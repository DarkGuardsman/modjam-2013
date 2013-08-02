package dark.common.hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dark.common.prefab.Pos;

public class HiveManager
{

    protected static Set<NetworkHivemind> hives = new HashSet<NetworkHivemind>();
    protected static HashMap<String, List<NetworkHivemind>> hivesets = new HashMap<String, List<NetworkHivemind>>();
    public static final String NEUTRIAL = "NEUT";

    /** Register a network to a list */
    public static void registerHive(NetworkHivemind mind)
    {
        if (!getHives().contains(mind))
        {
            hives.add(mind);
            String name = mind.getID();
            List<NetworkHivemind> list = new ArrayList<NetworkHivemind>();
            list.add(mind);
            if (hivesets.containsKey(name) && hivesets.get(name) != null)
            {
                list.addAll(hivesets.get(name));
            }
            hivesets.put(name, list);
        }
    }

    /** Removes a network from the list */
    public static void removeHive(NetworkHivemind mind)
    {
        hives.remove(mind);
        if (hivesets.containsKey(mind.getID()))
        {
            List<NetworkHivemind> list = hivesets.get(mind.getID());
            if (list == null)
            {
                hivesets.remove(mind.getID());
            }
            else
            {
                list.remove(mind);
                hivesets.put(mind.getID(), list);
            }
        }
    }

    public static Set<NetworkHivemind> getHives()
    {
        return hives;
    }

    /** Gets the string ID the bot or Tile will use to ID itself as part of the hive */
    public static String getHiveID(Object obj)
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
            for (NetworkHivemind entry : getHives())
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

    public static NetworkHivemind getHive(String hiveName)
    {
        return null;
    }
}
