package dark.common.hive.spire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.chunk.Chunk;
import dark.common.api.IHiveSpire;
import dark.common.gen.BuildingTickHandler;
import dark.common.gen.DarkSchematic;
import dark.common.hive.HiveManager;
import dark.common.hive.Hivemind;
import dark.common.prefab.BlockWrapper;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;
import dark.common.prefab.Trap;

/** Hive node that handles most of the work for the hive without getting in the main hives way */
public class HiveSpire implements IHiveSpire
{
    public static final int MAX_SIZE = 3;

    /** Static list of spire since they run outside the map */
    public static List<HiveSpire> staticList = new ArrayList<HiveSpire>();
    /** Level setting list <Level,<ScanRange,BuildDownAmount>> */
    public static HashMap<Integer, Pair<Integer, Integer>> level_List = new HashMap<Integer, Pair<Integer, Integer>>();
    /** Schematic names to build from for each level <Level,SchematicName> */
    public static HashMap<Integer, String> level_Schematic = new HashMap<Integer, String>();

    private PosWorld location;
    public DarkSchematic spireSchematic;
    private Hivemind hivemind;
    private String hiveName = "world";
    private int size = 1;
    private boolean built = false;
    private boolean loaded = false;
    /** List of traps loaded from the last schematic built with */
    public List<Trap> loadedTraps = new ArrayList<Trap>();
    /** List of inventories the spire can use to store items */
    List<IInventory> inventory = new ArrayList<IInventory>();

    static
    {
        //Pair.two for level list stacks with its levels before it
        level_List.put(1, new Pair<Integer, Integer>(10, 0));
        level_Schematic.put(1, "SpireOne");
        level_List.put(2, new Pair<Integer, Integer>(20, 18));
        level_Schematic.put(2, "SpireTwo");
        level_List.put(3, new Pair<Integer, Integer>(30, 0));
        level_Schematic.put(3, "SpireThree");
    }

    public HiveSpire(TileEntitySpire core)
    {
        this(new PosWorld(core.worldObj, new Pos(core)));
    }

    public HiveSpire(PosWorld pos)
    {
        location = pos;
        staticList.add(this);
        this.getHive().addToHive(this);
        this.init();
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

    /** Called by the spire's core block as soon as it loads into the world */
    public void init()
    {
        Chunk chunk = this.getLocation().world.getChunkFromBlockCoords(this.getLocation().x(), this.getLocation().z());
        if (chunk != null && chunk.isChunkLoaded)
        {
            this.scanArea();
        }
    }

    /** Destroys the spire's entity and marks everything for unload */
    public void setInvalid()
    {
        //TODO clear the spire and mark all elements for deletion
        //Case if the spire's core was removed and it can't re-populate the core
        this.getHive().remove(this);
        staticList.remove(this);
    }

    @Override
    public void loadSpire(NBTTagCompound nbt)
    {
        if (!this.loaded)
        {
            this.size = nbt.getInteger("Size");
        }
    }

    @Override
    public void saveSpire(NBTTagCompound nbt)
    {
        nbt.setInteger("Size", this.size);
    }

    /** Gets a list of entities within the spires scan range */
    public List<Entity> getEntitiesInRange()
    {
        List<Entity> entityList = new ArrayList<Entity>();
        int delta = size * 50;
        if (level_List.containsKey(this.size))
        {
            delta = level_List.get(this.size).getOne();
        }
        entityList.addAll(this.getLocation().world.getEntitiesWithinAABB(EntityLiving.class, new Pos(this.getLocation().xx + 0.5, this.getLocation().yy + 0.5, this.getLocation().zz + 0.5).expandBound(new Pos(delta, delta + 50, delta))));
        return entityList;
    }

    /** Called for each player in range of the spire per tick to trigger trap entities */
    public void triggerTrapIfNear(TileEntitySpire spire, EntityPlayer player)
    {
        if (player != null)
        {
            Pos pos = new Pos(player);
            Iterator<Trap> it = this.loadedTraps.iterator();
            while (it.hasNext())
            {

                Trap trap = it.next();

                System.out.println("Testing traps for " + player.username + " at " + pos.toString() + " trap at " + trap.pos);
                if (trap.canTrigger(player, pos))
                {
                    System.out.println("Trap Triggered by " + player.username);
                    if (trap.triggerTrap(this.getLocation().world))
                    {
                        BuildingTickHandler.markTrapReturn(this, trap, 10);
                        it.remove();
                    }
                }
            }
        }
    }

    /** Scans the entire structure of the spire looking for anything out of place. Also builds the
     * spire on first call */
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

        TileEntitySpire spire = null;
        double distance = Double.MAX_VALUE;
        boolean coreFound = false;

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
                    if (entity instanceof TileEntitySpire && new Pos(entity).getDistanceFrom(this.getLocation()) < distance)
                    {
                        spire = (TileEntitySpire) entity;
                        distance = new Pos(entity).getDistanceFrom(this.getLocation());
                        if (new Pos(entity).equals(this.getLocation()))
                        {
                            coreFound = true;
                        }
                    }
                    if (entity instanceof TileEntityChest && !inventory.contains(entity))
                    {
                        inventory.add((IInventory) entity);
                    }

                    scanList.put(pos, new Pair<Integer, Integer>(id, meta));
                }
            }
        }
        if (!coreFound && spire != null)
        {
            this.location = new PosWorld(spire.worldObj, new Pos(spire));
        }
        else if (!coreFound)
        {
            this.setInvalid();
        }
        //TODO compare schematic to scan list and mark for correction
    }

    /** Called to create the spire for the given level */
    public static void buildSpire(HiveSpire spire, int level)
    {
        System.out.println("Spire replicating itself at size " + level);

        if (level <= MAX_SIZE)
        {
            int drop = 0;
            for (int i = 0; i < level; i++)
            {
                Pair<Integer, Integer> pair = level_List.get(i);
                if (pair != null)
                {
                    drop += level_List.get(i).getTwo();
                }
            }
            String name = "SpireOne";
            spire.size = level;

            if (level_List.containsKey(level))
            {
                drop += level_List.get(level).getTwo();
            }
            if (level_Schematic.containsKey(level))
            {
                name = level_Schematic.get(level);
            }
            if (spire.spireSchematic == null || !spire.spireSchematic.fileName.equalsIgnoreCase(name))
            {
                spire.spireSchematic = new DarkSchematic(level_Schematic.get(level)).load();
            }
            spire.location.sub(new Pos(0, drop, 0));

            if (spire.spireSchematic != null)
            {
                int path = new Random().nextBoolean() ? 1 : 2;
                spire.spireSchematic.buildSpire(spire, true, true, path);
                spire.size = level;
                spire.loadTraps();
            }
        }

    }

    /** Called to load traps from the current build schematic */
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
        if(this.location == null)
        {
            this.location = new PosWorld();
        }
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
        if (obj instanceof BlockWrapper)
        {
            Block block = ((BlockWrapper) obj).block;
            PosWorld pos = ((BlockWrapper) obj).pos;
            if (pos != null && pos.world == this.getLocation().world && block != null)
            {
                //TODO call some event or drones to this location
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
