package dark.common.helpers;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class NetworkHivemind
{
    /** Used in the off chance the core location was not set */
    public static Pos backupCoreLocation = new Pos(0, 126, 0);

    private PosWorld hiveCoreLocation;
    private String hiveID = "world";

    Set<TileEntity> hiveTiles = new HashSet<TileEntity>();
    Set<Entity> hiveBots = new HashSet<Entity>();

    public NetworkHivemind(PosWorld coreLocation, String hiveID, Object... hiveObjects)
    {
        this.hiveCoreLocation = coreLocation;
        this.hiveID = hiveID;
        for (int i = 0; hiveObjects != null && i < hiveObjects.length; i++)
        {
            this.addToHive(hiveObjects[i]);
        }
    }

    public PosWorld getLocation()
    {
        if (this.hiveCoreLocation == null)
        {
            this.hiveCoreLocation = new PosWorld(DimensionManager.getWorld(0), backupCoreLocation);
        }
        return this.hiveCoreLocation;
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

    public String getID()
    {
        if (this.hiveID == null || this.hiveID.isEmpty())
        {
            this.hiveID = "world";
        }
        return this.hiveID;
    }
}
