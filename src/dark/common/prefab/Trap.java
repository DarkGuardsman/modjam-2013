package dark.common.prefab;

import net.minecraft.nbt.NBTTagCompound;

public class Trap
{
    public Pos pos;
    public String type;

    public Trap(Pos pos, String type)
    {
        this.pos = pos;
        this.type = type;
    }

    public void save(NBTTagCompound nbt)
    {
        nbt.setString("type", type);
        nbt.setCompoundTag("start", pos.save(new NBTTagCompound()));
    }

    public static Trap load(NBTTagCompound nbt)
    {
        return new Trap(new Pos().load(nbt.getCompoundTag("start")), nbt.getString("type"));
    }

    @Override
    public String toString()
    {
        return pos.toString() + " " + type;
    }
}
