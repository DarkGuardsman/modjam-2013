package dark.common.hive;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Save;
import dark.common.api.IHiveObject;
import dark.common.api.IHiveSpire;
import dark.common.gen.NBTFileSaver;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class Hivemind implements IHiveObject
{
    /** Used in the off chance the core location was not set */
    public static Pos backupCoreLocation = new Pos(0, 126, 0);

    private PosWorld hiveCoreLocation;
    private String hiveID = "world";

    /** Machines (Sentries, processors, builders) */
    public Set<TileEntity> hiveTiles = new HashSet<TileEntity>();
    /** Entities(Robots) */
    public Set<Entity> hiveBots = new HashSet<Entity>();

    public Set<IHiveSpire> spires = new HashSet<IHiveSpire>();

    public Hivemind(PosWorld coreLocation, String hiveID, Object... hiveObjects)
    {
        this.hiveCoreLocation = coreLocation;
        this.hiveID = hiveID;
        for (int i = 0; hiveObjects != null && i < hiveObjects.length; i++)
        {
            this.addToHive(hiveObjects[i]);
        }
        HiveManager.registerHive(this);
    }

    public PosWorld getLocation()
    {
        if (this.hiveCoreLocation == null)
        {
            this.hiveCoreLocation = new PosWorld(DimensionManager.getWorld(0), backupCoreLocation);
        }
        return this.hiveCoreLocation;
    }

    public void invalidate()
    {
        HiveManager.removeHive(this);
        for (TileEntity entity : hiveTiles)
        {
            if (entity instanceof IHiveObject)
            {
                ((IHiveObject) entity).setHiveID(HiveManager.NEUTRIAL);
            }
        }
        for (Entity entity : hiveBots)
        {
            if (entity instanceof IHiveObject)
            {
                ((IHiveObject) entity).setHiveID(HiveManager.NEUTRIAL);
            }
        }

        for (IHiveSpire entity : spires)
        {
            ((IHiveObject) entity).setHiveID(HiveManager.NEUTRIAL);
        }
    }

    public void addToHive(Object obj)
    {
        if (obj instanceof Entity)
        {
            hiveBots.add((Entity) obj);
        }
        if (obj instanceof TileEntity)
        {
            hiveTiles.add((TileEntity) obj);
        }
        if (obj instanceof IHiveSpire)
        {
            spires.add((IHiveSpire) obj);
        }
    }

    public void remove(Object obj)
    {
        if (obj instanceof Entity)
        {
            hiveBots.remove((Entity) obj);
        }
        if (obj instanceof TileEntity)
        {
            hiveTiles.remove((TileEntity) obj);

        }
    }

    @Override
    public void setHiveID(String id)
    {
        this.hiveID = id;
        this.refresh();
    }

    @Override
    public String getHiveID()
    {
        if (this.hiveID == null || this.hiveID.isEmpty())
        {
            this.hiveID = "world";
        }
        return this.hiveID;
    }

    public void refresh()
    {
        //TODO refresh the hive and remove any invalid objects. As well reset the hive id in all objects
    }

    public IHiveSpire getClosestSpire(Object obj)
    {
        IHiveSpire hive = null;
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
        else if (obj instanceof IHiveSpire)
        {
            pos = new Pos(((IHiveSpire) obj).getLocation().xx, ((IHiveSpire) obj).getLocation().yy, ((IHiveSpire) obj).getLocation().zz);
            world = ((IHiveSpire) obj).getLocation().world;
        }

        return hive;
    }

    public void merger(Hivemind mind)
    {
        if (mind.getHiveID() == this.getHiveID())
        {

        }

    }

    @ForgeSubscribe
    public void onWorldSave(Save event)
    {
        System.out.println("Hivemind has received a save event for dimID " + (event != null && event.world != null ? event.world.provider.dimensionId : "null"));
        if (event != null && event.world != null && event.world == this.getLocation().world)
        {
            for (IHiveSpire spire : this.spires)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("xCoord", spire.getLocation().x());
                tag.setInteger("yCoord", spire.getLocation().y());
                tag.setInteger("zCoord", spire.getLocation().z());
                tag.setString("HiveID", this.getHiveID());
                spire.saveSpire(tag);
                NBTFileSaver.saveNBTFile("HiveSpire_" + this.getHiveID() + "_" + this.getLocation().world.provider.dimensionId + "_", NBTFileSaver.getSaveFolder(), tag, true);
            }
        }
    }
}
