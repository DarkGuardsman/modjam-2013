package dark.common.hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dark.common.prefab.Pos;

public class HiveManager
{
    /** List of loaded hiveminds */
    protected static List<Hivemind> hives = new ArrayList<Hivemind>();
    /** Map of hives with the same hivemind name */
    protected static HashMap<String, List<Hivemind>> hivesets = new HashMap<String, List<Hivemind>>();
    /** Neutral hive ID, all hive objects default to this */
    public static final String NEUTRIAL = "NEUT";
    /** Neutral hivemind used to store hive objects as the try to load a non-neutral hive */
    public static final Hivemind NEUT_HIVE = new Hivemind(null, NEUTRIAL);
    //MinecraftServer.worldServers.world.provider.dimensionId

    static
    {
        registerHive(NEUT_HIVE);
    }

    /** Register a network to a list */
    public static void registerHive(Hivemind mind)
    {
        if (!hives.contains(mind))
        {
            hives.add(mind);
            String name = mind.getHiveID();
            List<Hivemind> list = new ArrayList<Hivemind>();
            list.add(mind);
            if (hivesets.containsKey(name) && hivesets.get(name) != null)
            {
                list.addAll(hivesets.get(name));
            }
            hivesets.put(name, list);
        }
    }

    /** Removes a network from the list */
    public static void removeHive(Hivemind mind)
    {
        hives.remove(mind);
        changeHiveTag(mind, null);

    }

    /** Used to change a hiveminds tag id and update the hiveset list at the same time
     *
     * @param mind - hivemind
     * @param tag - null will remove it from tag list */
    public static void changeHiveTag(Hivemind mind, String tag)
    {
        if (hivesets.containsKey(mind.getHiveID()))
        {
            List<Hivemind> list = hivesets.get(mind.getHiveID());
            if (list == null)
            {
                hivesets.remove(mind.getHiveID());
            }
            else
            {
                list.remove(mind);
                hivesets.put(mind.getHiveID(), list);
            }
        }
        if (tag != null)
        {
            mind.setHiveID(tag);

            if (hivesets.containsKey(tag))
            {
                List<Hivemind> list = hivesets.get(tag);
                if (list == null)
                {
                    list = new ArrayList<Hivemind>();
                }
                list.add(mind);
                hivesets.put(tag, list);
            }
        }
    }

    public static List<Hivemind> getHives()
    {
        return hives;
    }

    /** Gets the string ID the bot or Tile will use to ID itself as part of the hive */
    public static String getHiveID(Object obj)
    {
        Hivemind hive = null;
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
            for (Hivemind entry : getHives())
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
                return hive.getHiveID();
            }
        }

        return NEUTRIAL;
    }

    public void cleanup()
    {
        Iterator<Hivemind> it = HiveManager.getHives().iterator();
        while (it.hasNext())
        {
            Hivemind mind = it.next();
            /** Remove hivemind instances that don't have any parts */
            if (mind.hiveBots.size() < 0 && mind.hiveTiles.size() < 0 && mind.spires.size() < 0)
            {
                it.remove();
                changeHiveTag(mind, null);
            }
        }
    }

    public static Hivemind getHive(String hiveName)
    {
        return hivesets.get(hiveName) != null ? hivesets.get(hiveName).get(0) : null;
    }
}
