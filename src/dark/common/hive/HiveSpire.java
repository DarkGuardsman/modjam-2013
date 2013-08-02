package dark.common.hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import dark.common.api.IHiveSpire;
import dark.common.prefab.PosWorld;

/** Hive node that handles most of the work for the hive without getting in the main hives way */
public class HiveSpire implements IHiveSpire
{
    PosWorld location;
    Hivemind hivemind;
    String hiveName = "world";
    int side = 1;
    List<IInventory> inventory = new ArrayList<IInventory>();

    public void scanArea()
    {

    }

    @Override
    public Hivemind getHive()
    {
        if (hivemind == null || !hivemind.getHiveID().equalsIgnoreCase(hiveName))
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
        if (obj instanceof Entity)
        {
            if (!this.getHive().hiveBots.contains(obj))
            {
                this.getHive().hiveBots.add((Entity) obj);
            }
        }

    }

    @Override
    public void reportDeath(Object obj)
    {
        if (obj instanceof Entity)
        {
            if (!this.getHive().hiveBots.contains(obj))
            {
                this.getHive().hiveBots.remove((Entity) obj);
            }
        }

    }

    @Override
    public void receivedItems(ItemStack stackIn, Object obj)
    {
        if (stackIn != null)
        {
            ItemStack stack = stackIn.copy();
            stack.stackSize = 1;
            int a = stackIn.stackSize;
            if (this.containedItems.containsKey(stack))
            {
                a += this.containedItems.get(stack);
            }
            this.containedItems.put(stack, a);

        }

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
