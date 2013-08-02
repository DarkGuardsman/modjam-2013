package dark.common.prefab;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Pos
{
    double xx, yy, zz;

    public Pos(TileEntity entity)
    {
        this.xx = entity.xCoord;
        this.yy = entity.yCoord;
        this.zz = entity.zCoord;
    }

    public Pos(Entity entity)
    {
        this.xx = entity.posX;
        this.yy = entity.posY;
        this.zz = entity.posZ;
    }

    public Pos(double x, double y, double z)
    {
        this.xx = x;
        this.yy = y;
        this.zz = z;
    }

    public int x()
    {
        return (int) xx;
    }

    public int y()
    {
        return (int) yy;
    }

    public int z()
    {
        return (int) zz;
    }

    public int getBlockMeta(World world)
    {
        return world.getBlockMetadata(this.x(), this.y(), this.z());
    }

    public int getBlockID(World world)
    {
        return world.getBlockId(this.x(), this.y(), this.z());
    }

    public Pos modifyBy(ForgeDirection direction, double lenght)
    {
        this.xx += direction.offsetX * lenght;
        this.yy += direction.offsetY * lenght;
        this.zz += direction.offsetZ * lenght;
        return this;
    }
}
