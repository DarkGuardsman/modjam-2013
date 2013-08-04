package dark.common.prefab;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PosWorld extends Pos
{
    public World world;

    public PosWorld()
    {
        super();
        this.world = DimensionManager.getWorld(0);
    }

    public PosWorld(World world, Pos pos)
    {
        this(world, pos.xx, pos.yy, pos.zz);
    }

    public PosWorld(World world, double x, double y, double z)
    {
        super(x, y, z);
        this.world = world;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound tag)
    {
        tag.setInteger("dim", this.world.provider.dimensionId);
        return super.save(tag);
    }

    @Override
    public PosWorld load(NBTTagCompound tag)
    {
        super.load(tag);
        this.world = DimensionManager.getWorld(tag.getInteger("dim"));
        return this;
    }

    @Override
    public String toString()
    {
        return world.provider.dimensionId + "D " + x() + "X " + y() + "Y " + z() + "Z ";
    }

    @Override
    public boolean equals(Object paramObject)
    {
        if (super.equals(paramObject) && paramObject instanceof PosWorld)
        {
            PosWorld pos = (PosWorld) paramObject;
            return this.world == pos.world;
        }
        return false;
    }

    public int getBlockID()
    {
        return this.getBlockID(this.world);
    }

    public int getBlockMeta()
    {
        return this.getBlockMeta(this.world);
    }

}
