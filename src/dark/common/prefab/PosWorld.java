package dark.common.prefab;

import net.minecraft.world.World;

public class PosWorld extends Pos
{
    public World world;

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

}
