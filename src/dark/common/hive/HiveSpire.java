package dark.common.hive;

import net.minecraft.item.ItemStack;
import dark.common.api.IHiveSpire;
import dark.common.prefab.PosWorld;

/** Hive node that handles most of the work for the hive without getting in the main hives way */
public class HiveSpire implements IHiveSpire
{
    PosWorld location;
    Hivemind hivemind;
    String hiveName = "world";

    @Override
    public Hivemind getHive()
    {
        if (hivemind == null || !hivemind.getID().equalsIgnoreCase(hiveName))
        {
            this.hivemind = HiveManager.getHive(this.getHiveID());
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

    @Override
    public void setHiveID(String id)
    {
        this.hiveName = id;

    }

    @Override
    public String getHiveID()
    {
        if (this.hiveName.equalsIgnoreCase("world") || this.hiveName.equalsIgnoreCase(HiveManager.NEUTRIAL))
        {
            this.hiveName = HiveManager.getHiveID(this);
        }
        return this.hiveName;
    }

}
