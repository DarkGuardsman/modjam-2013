package dark.common.hive.spire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import dark.common.api.IHiveSpire;
import dark.common.gen.DarkSchematic;
import dark.common.gen.NBTFileSaver;
import dark.common.hive.HiveManager;
import dark.common.hive.Hivemind;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;
import dark.common.prefab.Trap;

/** Hive node that handles most of the work for the hive without getting in the main hives way */
public class HiveSpire implements IHiveSpire
{
    public static final int MAX_SIZE = 2;

    /** Static list of spire since they run outside the map */
    public static List<HiveSpire> staticList = new ArrayList<HiveSpire>();
    public static HashMap<Integer, Pair<Integer, Integer>> level_List = new HashMap<Integer, Pair<Integer, Integer>>();
    public static HashMap<Integer, String> level_Schematic = new HashMap<Integer, String>();

    PosWorld location;
    public DarkSchematic spireSchematic;
    Hivemind hivemind;
    String hiveName = "world";

    public List<Trap> loadedTraps = new ArrayList<Trap>();
    private int size = 0;
    private boolean built = false;
    private boolean loaded = false;

    List<IInventory> inventory = new ArrayList<IInventory>();

    static
    {
        level_List.put(1, new Pair<Integer, Integer>(10, 0));
        level_Schematic.put(1, "SpireOne");
        level_List.put(2, new Pair<Integer, Integer>(20, 18));
        level_Schematic.put(2, "SpireTwo");
    }

    public HiveSpire(TileEntitySpire core)
    {
        staticList.add(this);
        location = new PosWorld(core.worldObj, new Pos(core));
    }

    /** Gets a spire close to the location. Use mainly if a spire core unloaded from the map and
     * needs to get its spire instance back to save from creating a new one
     *
     * @param location - world location
     * @param i - max distance to search for the spire
     * @return */
    public static HiveSpire getSpire(PosWorld location, int i)
    {
        if (location != null)
        {
            double distance = Double.MAX_VALUE;
            HiveSpire spire = null;
            for (HiveSpire entry : staticList)
            {
                double distanceTo = entry.getLocation().getDistanceFrom(location);
                if (entry.getLocation().world == location.world && distanceTo < distance && distanceTo < i)
                {
                    spire = entry;
                    distance = distanceTo;
                }
            }
            return spire;
        }
        return null;
    }

    public void init()
    {
        this.getHive().addToHive(this);
        this.scanArea();
        if(!this.loaded)
        {
            this.loaded = true;
        }
    }

    public void setInvalid()
    {
        //TODO clear the spire and mark all elements for deletion
        //Case if the spire's core was removed and it can't re-populate the core
    }

    @Override
    public void loadSpire(NBTTagCompound nbt)
    {
        this.size = nbt.getInteger("Size");
    }

    @Override
    public void saveSpire(NBTTagCompound nbt)
    {
        nbt.setInteger("Size", this.size);
    }

    public List<Entity> getEntitiesInRange()
    {
        List<Entity> entityList = new ArrayList<Entity>();
        int delta = size * 50;
        if (level_List.containsKey(this.size))
        {
            delta = level_List.get(this.size).getOne();
        }
        entityList.addAll(this.getLocation().world.getEntitiesWithinAABB(Entity.class, new Pos(this.getLocation().xx + 0.5, this.getLocation().yy + 0.5, this.getLocation().zz + 0.5).expandBound(new Pos(delta, delta + 50, delta))));
        return entityList;
    }

    public void triggerTrapIfNear(TileEntitySpire spire, EntityPlayer player)
    {
        if (player != null)
        {
            Pos pos = new Pos(player);
            Iterator<Trap> it = this.loadedTraps.iterator();
            while (it.hasNext())
            {
                Trap trap = it.next();
                if (trap.type.equalsIgnoreCase("fall"))
                {
                    if (pos.getDistanceFrom(trap.pos) < 3)
                    {
                        System.out.println("Trap reset " + trap.toString());
                        spire.markTrapReturn(trap, 10, new Pair<Integer, Integer>(pos.getBlockID(this.getLocation().world), pos.getBlockMeta(this.getLocation().world)));
                        pos.setBlock(this.getLocation().world, 0);
                        it.remove();

                    }
                }
            }
        }
    }

    public void scanArea()
    {
        //TODO add one mine timer to suck up all items and store them
        if (this.built = false)
        {
            buildSpire(this, this.size);
            this.built = true;
        }
        System.out.println("Spire scanning itself for damage at " + getLocation().x() + "x " + getLocation().y() + "y " + getLocation().z() + "z ");
        HashMap<Pos, Pair<Integer, Integer>> scanList = new HashMap<Pos, Pair<Integer, Integer>>();

        int delta = size * 5;
        if (level_List.containsKey(this.size))
        {
            delta = level_List.get(this.size).getOne();
        }
        Pos start = new Pos(getLocation().xx + delta, Math.min(getLocation().yy + delta, 255), getLocation().zz + delta);
        Pos end = new Pos(getLocation().xx - delta, Math.max(getLocation().yy - delta, 6), getLocation().zz - delta);

        for (int y = start.y(); y <= start.y() && y >= end.y(); y--)
        {
            for (int x = start.x(); x <= start.x() && x >= end.x(); x--)
            {
                for (int z = start.z(); z <= start.z() && z >= end.z(); z--)
                {
                    Pos pos = new Pos(x, y, z);

                    int id = pos.getBlockID(getLocation().world);
                    int meta = pos.getBlockMeta(getLocation().world);

                    Block block = Block.blocksList[id];
                    TileEntity entity = pos.getTileEntity(getLocation().world);

                    if (entity instanceof TileEntityChest && !inventory.contains(entity))
                    {
                        inventory.add((IInventory) entity);
                    }

                    scanList.put(pos, new Pair<Integer, Integer>(id, meta));
                }
            }
        }
        //TODO compare schematic to scan list and mark for correction
    }

    public static void buildSpire(HiveSpire spire, int level)
    {
        System.out.println("Spire replicating itself at size " + level);

        if (level <= MAX_SIZE)
        {
            int drop = 0;
            String name = "SpireOne";
            spire.size = level;

            if (level_List.containsKey(level))
            {
                drop = level_List.get(level).getTwo();
            }
            if (level_Schematic.containsKey(level))
            {
                name = level_Schematic.get(level);
            }
            if (spire.spireSchematic == null || !spire.spireSchematic.fileName.equalsIgnoreCase(name))
            {
                spire.spireSchematic = new DarkSchematic(level_Schematic.get(level)).load();
            }
            spire.location.sub(new Pos(0, -drop, 0));

            if (spire.spireSchematic != null)
            {
                int path = new Random().nextBoolean() ? 1 : 2;
                spire.spireSchematic.buildSpire(spire, true, true, path);
                spire.size = level;
                spire.loadTraps();
            }
        }

    }

    private void loadTraps()
    {
        if (this.spireSchematic != null)
        {
            NBTTagCompound traps = this.spireSchematic.extraData.getCompoundTag("traps");
            int count = traps.getInteger("count");
            List<Trap> trapList = new ArrayList<Trap>();
            Pos corner = this.getLocation().sub(this.spireSchematic.getCenter());
            for (int i = 0; i < count; i++)
            {
                NBTTagCompound trap = traps.getCompoundTag("trap" + i);
                if (trap != null)
                {
                    Trap lTrap = Trap.load(trap);
                    lTrap.pos.add(corner);
                    trapList.add(lTrap);
                    System.out.println("loaded Trap " + lTrap.toString());
                }
            }
            this.loadedTraps.clear();
            this.loadedTraps.addAll(trapList);
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
