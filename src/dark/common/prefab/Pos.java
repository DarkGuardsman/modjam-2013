package dark.common.prefab;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Pos implements Cloneable
{
    public double xx;
    public double yy;
    public double zz;

    public Pos()
    {
        this.xx = 0;
        this.yy = 0;
        this.zz = 0;
    }

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

    @Override
    public Pos clone()
    {
        return new Pos(this.xx, this.yy, this.zz);
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

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public double getMagnitudeSquared()
    {
        return xx * xx + yy * yy + zz * zz;
    }

    public int getBlockMeta(World world)
    {
        return world.getBlockMetadata(this.x(), this.y(), this.z());
    }

    public int getBlockID(World world)
    {
        return world.getBlockId(this.x(), this.y(), this.z());
    }

    public void setBlock(World world, int blockID)
    {
        this.setBlock(world, blockID, 0);
    }

    public void setBlock(World world, int blockID, int meta)
    {
        this.setBlock(world, blockID, meta, 3);
    }

    public void setBlock(World world, int blockID, int meta, int flag)
    {
        world.setBlock(x(), y(), z(), blockID, meta, flag);
    }

    public void modifyBy(ForgeDirection direction)
    {
        this.modifyBy(direction, 1);

    }

    public Pos modifyBy(ForgeDirection direction, double lenght)
    {
        this.xx += direction.offsetX * lenght;
        this.yy += direction.offsetY * lenght;
        this.zz += direction.offsetZ * lenght;
        return this;
    }

    public double getDistanceFrom(Pos pos)
    {
        double deltaX = xx - pos.xx;
        double deltaY = yy - pos.yy;
        double deltaZ = zz - pos.zz;
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getBlockTileEntity(x(), y(), z());
    }

    @Override
    public String toString()
    {
        return x() + "X " + y() + "Y " + z() + "Z ";
    }

    public Pos multi(double d)
    {
        this.xx *= d;
        this.yy *= d;
        this.zz *= d;
        return this;

    }

    public void save(NBTTagCompound tag)
    {
        tag.setDouble("xx", xx);
        tag.setDouble("yy", yy);
        tag.setDouble("zz", zz);
    }

    public Pos load(NBTTagCompound tag)
    {
        this.xx = tag.getDouble("xx");
        this.yy = tag.getDouble("yy");
        this.zz = tag.getDouble("zz");
        return this;
    }
}
