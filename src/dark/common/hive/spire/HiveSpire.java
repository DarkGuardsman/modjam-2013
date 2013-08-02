package dark.common.hive.spire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import dark.common.api.IHiveSpire;
import dark.common.hive.HiveManager;
import dark.common.hive.Hivemind;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

/** Hive node that handles most of the work for the hive without getting in the main hives way */
public class HiveSpire implements IHiveSpire
{
    public static List<HiveSpire> staticList = new ArrayList<HiveSpire>();
    PosWorld location;
    Hivemind hivemind;
    String hiveName = "world";
    int size = 1;
    List<IInventory> inventory = new ArrayList<IInventory>();

    public HiveSpire(TileEntitySpire core)
    {
        staticList.add(this);
        location = new PosWorld(core.worldObj, new Pos(core));
    }

    public static HiveSpire getSpire(PosWorld location)
    {

    }

    public void init()
    {
       this.getHive().addToHive(this);
       this.scanArea();
    }

    public void scanArea()
    {
        System.out.print("Spire scanning itself for damage at " + getLocation().x() + "x " + getLocation().y() + "y " + getLocation().z() + "z ");
        int delta = size * 50;
        Pos start = new Pos(getLocation().xx + delta, Math.min(getLocation().yy + delta, 255), getLocation().zz + delta);
        Pos end = new Pos(getLocation().xx - delta, Math.max(getLocation().yy - delta, 0), getLocation().zz - delta);
        int x, y, z;
        for (y = start.y(); y <= start.y() && y >= end.y(); y--)
        {
            for (x = start.x(); x <= start.x() && x >= end.x(); x--)
            {
                for (z = start.z(); z <= start.z() && z >= end.z(); z--)
                {
                    this.onScanBlock(new Pos(x, y, z));
                }
            }
        }
    }

    public void buildSpire(int level)
    {
        if(level == 0)
        {

        }
    }

    public void onScanBlock(Pos pos)
    {
        int id = pos.getBlockID(getLocation().world);
        int meta = pos.getBlockMeta(getLocation().world);
        Block block = Block.blocksList[id];
        TileEntity entity = pos.getTileEntity(getLocation().world);
        if (entity instanceof TileEntityChest && !inventory.contains(entity))
        {
            inventory.add((IInventory) entity);
        }
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
