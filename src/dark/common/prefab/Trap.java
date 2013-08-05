package dark.common.prefab;

import java.util.HashMap;

import dark.common.gen.TrapFall;
import dark.common.gen.TrapSpawn;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

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

    public void triggerTrap()
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

    @Override
    public String toString()
    {
        return pos.toString() + " " + type;
    }
}
