package dark.common.hive;

import net.minecraft.item.ItemStack;
import dark.common.api.IHiveSpire;
import dark.common.prefab.PosWorld;

/** Hive node that handles most of the work for the hive without getting in the main hives way */
public class HiveSpire implements IHiveSpire
{
    PosWorld location;
    NetworkHivemind hivemind;
    String hiveName = "world";


    public NetworkHivemind getHive()
    {
        if(this.hiveName.equalsIgnoreCase("world"))
        {
            HiveManager.getHiveID(this);
        }
        if(hivemind == null)
        {
            this.hivemind = HiveManager.getHive(hiveName);
        }
        return hivemind;
    }
    @Override
    public PosWorld getLocation()
    {
        return location;
    }

    @Override
    public void reportIn(Object obj)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void reportDeath(Object obj)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivedItems(ItemStack stack, Object obj)
    {
        // TODO Auto-generated method stub

    }

}
