package dark.common.prefab;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class Trap
{
    public Pos pos;
    public String type;
    public int resetTime;

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
        nbt.setString("type", type);
        nbt.setCompoundTag("start", pos.save(new NBTTagCompound()));
        nbt.setInteger("reset", resetTime);
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
