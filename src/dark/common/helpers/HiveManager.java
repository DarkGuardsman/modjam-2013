package dark.common.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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

    public String getHiveID(Object obj)
    {
        NetworkHivemind hive = null;
        double distance = Double.MAX_VALUE;
        for (NetworkHivemind entry : hives)
        {
           double distanceTo =
        }

        return NEUTRIAL;
    }
}
