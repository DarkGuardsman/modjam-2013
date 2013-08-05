package dark.common.prefab;

import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import dark.common.gen.TrapFall;
import dark.common.gen.TrapSpawn;

public class Trap
{
    public Pos pos;
    public String type;
    public int resetTime;
    public static HashMap<String, Class<? extends Trap>> trapMap = new HashMap<String, Class<? extends Trap>>();
    public static HashMap<Class<? extends Trap>, String> classMap = new HashMap<Class<? extends Trap>, String>();

    static
    {
        trapMap.put("fall", TrapFall.class);
        trapMap.put("spawn", TrapSpawn.class);
    }

    public Trap(Pos pos, String type, int resetTime)
    {
        this.pos = pos;
        this.type = type;
        this.resetTime = resetTime;
    }

    public boolean canTrigger(Entity entity, Pos pos)
    {
        return false;
    }

    public boolean triggerTrap(World world)
    {
        return false;
    }

    public void reset(World world)
    {

    }

    public void save(NBTTagCompound nbt)
    {
        String s = (String) classMap.get(this.getClass());

        if (s == null)
        {
            throw new RuntimeException(this.getClass() + " trap is missing a mapping! This is a bug!");
        }
        else
        {
            nbt.setString("type", type);
            nbt.setCompoundTag("start", pos.save(new NBTTagCompound()));
            nbt.setInteger("reset", resetTime);
        }
    }

    public static Trap load(NBTTagCompound nbt)
    {
        return new Trap(new Pos().load(nbt.getCompoundTag("start")), nbt.getString("type"), nbt.getInteger("reset"));
    }

    public static Trap createAndLoadEntity(NBTTagCompound par0NBTTagCompound)
    {
        Trap trap = null;

        Class oclass = null;

        try
        {
            oclass = (Class) trapMap.get(par0NBTTagCompound.getString("id"));

            if (oclass != null)
            {
                trap = (Trap) oclass.newInstance();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (trap != null)
        {
            try
            {
                trap.load(par0NBTTagCompound);
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "A Trap %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author", par0NBTTagCompound.getString("id"), oclass.getName());
                trap = null;
            }
        }
        else
        {
            MinecraftServer.getServer().getLogAgent().logWarning("Skipping Trap with id " + par0NBTTagCompound.getString("id"));
        }

        return trap;
    }

    @Override
    public String toString()
    {
        return pos.toString() + " " + type;
    }
}
