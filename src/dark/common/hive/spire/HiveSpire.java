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
import dark.common.entity.EntityBossGigus;
import dark.common.entity.EntityDefender;
import dark.common.gen.BuildingTickHandler;
import dark.common.gen.DarkSchematic;
import dark.common.gen.TrapSpawn;
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
    public static final int MAX_SIZE = 1;

    /** Static list of spire since they run outside the map */
    public static List<HiveSpire> staticList = new ArrayList<HiveSpire>();
    /** Level setting list -Level,-ScanRange,BuildDownAmount-- */
    public static HashMap<Integer, Pair<Integer, Integer>> level_List = new HashMap<Integer, Pair<Integer, Integer>>();
    /** Schematic names to build from for each level -Level,SchematicName- */
    public static HashMap<Integer, String> level_Schematic = new HashMap<Integer, String>();
    /** Location of the spire. Don't call this var instead use getLocation() */
    private PosWorld location;
    /** Currently loaded schematic */
    public DarkSchematic schematic;
    /** Linked hivemind that controls this spire */
    private Hivemind hivemind;
    /** Hive name */
    private String hiveName = "world";
    /** current size of the spire */
    protected int size = 1;
    /** Has been built after being created */
    private boolean built = false;
    /** Has loaded from save file */
    private boolean loaded = false;
    /** List of traps loaded from the last schematic built with */
    public List<Trap> loadedTraps = new ArrayList<Trap>();
    /** List of inventories the spire can use to store items */
    List<IInventory> inventory = new ArrayList<IInventory>();
    /** Counter of how many drones have been killed in its range */
    private int deaths = 0;

    static
    {
        //Pair.two for level list stacks with its levels before it
        level_List.put(1, new Pair<Integer, Integer>(30, 18));
        level_Schematic.put(1, "SpireTwo");
        //level_List.put(2, new Pair<Integer, Integer>(20, 18));
        //level_Schematic.put(2, "SpireThree");
    }

    public HiveSpire(TileEntitySpireCore core)
    {
        this(new PosWorld(core.worldObj, new Pos(core)));
    }

    public HiveSpire(PosWorld pos)
    {
        location = pos;
        synchronized (staticList)
        {
            staticList.add(this);
        }
        this.getHive().addToHive(this);
    }

    /** Gets the schematic for the current level of spire */
    public DarkSchematic getSchematic()
    {
        String name = level_Schematic.get(this.size);
        if (this.schematic == null || !this.schematic.fileName.equalsIgnoreCase(name))
        {
            this.schematic = new DarkSchematic(name).load();
        }
        return this.schematic;
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
            synchronized (staticList)
            {
                for (HiveSpire entry : staticList)
                {
                    double distanceTo = entry.getLocation().getDistanceFrom2D(location);
                    if (entry.getLocation().world == location.world && distanceTo < distance && distanceTo < i)
                    {
                        spire = entry;
                        distance = distanceTo;
                    }
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
        this.getHive().remove(this);'
        synchronized (staticList)
        {
            staticList.remove(this);
        }
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
    @SuppressWarnings("unchecked")
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
    public void triggerTrapIfNear(TileEntitySpireCore spire, EntityPlayer player)
    {
        if (player != null)
        {
            Pos pos = new Pos(player);
            if (this.getLocation().getDistanceFrom(pos) < 150)
            {
                if (this.loadedTraps.size() == 0 && this.getSchematic() != null)
                {
                    this.getSchematic().loadTraps(this);
                }
                Iterator<Trap> it = this.loadedTraps.iterator();
                while (it.hasNext())
                {

                    Trap trap = it.next();
                    if (trap.canTrigger(player, pos))
                    {
                        System.out.println("Trap Triggered by " + player.username + " at location " + pos.toString());
                        if (trap.triggerTrap(this.getLocation().world))
                        {
                            BuildingTickHandler.markTrapReturn(this, trap, 10);
                            it.remove();
                        }
                    }
                }
                int si = this.getLocation().world.getEntitiesWithinAABB(EntityDefender.class, new Pos(this.getLocation().xx + 0.5, this.getLocation().yy + 0.5, this.getLocation().zz + 0.5).expandBound(new Pos(100, 100 + 50, 100))).size();
                int s2 = this.getLocation().world.getEntitiesWithinAABB(EntityBossGigus.class, new Pos(this.getLocation().xx + 0.5, this.getLocation().yy + 0.5, this.getLocation().zz + 0.5).expandBound(new Pos(100, 100 + 50, 100))).size();

                if (this.deaths > 50 && s2 < 1)
                {
                    EntityDefender entity = new EntityDefender(player.worldObj);
                    entity.setPosition(player.posX + this.getLocation().world.rand.nextInt(5), player.posY + this.getLocation().world.rand.nextInt(5), player.posZ + this.getLocation().world.rand.nextInt(5));
                    player.worldObj.spawnEntityInWorld(entity);
                    entity.playLivingSound();
                }
                else if (si < 30 && this.getLocation().world.rand.nextInt(this.deaths > 20 ? 2 : 10) == 1)
                {
                    TrapSpawn trap = new TrapSpawn(new Pos(player).add(new Pos(this.getLocation().world.rand.nextInt(5), this.getLocation().world.rand.nextInt(1), this.getLocation().world.rand.nextInt(5))));
                    trap.triggerTrap(player.worldObj);
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
        //System.out.println("Spire scanning itself for damage at " + getLocation().x() + "x " + getLocation().y() + "y " + getLocation().z() + "z ");
        HashMap<Pos, Pair<Integer, Integer>> scanList = new HashMap<Pos, Pair<Integer, Integer>>();

        int delta = size * 5;
        if (level_List.containsKey(this.size))
        {
            delta = level_List.get(this.size).getOne();
        }
        Pos start = new Pos(getLocation().xx + delta, Math.min(getLocation().yy + delta, 255), getLocation().zz + delta);
        Pos end = new Pos(getLocation().xx - delta, Math.max(getLocation().yy - delta, 6), getLocation().zz - delta);

        TileEntitySpireCore spire = null;
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
                    if (entity instanceof TileEntitySpireCore && new Pos(entity).getDistanceFrom(this.getLocation()) < distance)
                    {
                        spire = (TileEntitySpireCore) entity;
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
        //System.out.println("Spire replicating itself at size " + level);

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
            spire.location.sub(new Pos(0, drop, 0));

            if (spire.getSchematic() != null)
            {
                int path = new Random().nextBoolean() ? 1 : 2;
                spire.getSchematic().buildSpire(spire, true, true, path);
                spire.size = level;
                spire.getSchematic().loadTraps(spire);
            }
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
        if (this.location == null)
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
            this.deaths++;
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
