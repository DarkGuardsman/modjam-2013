package dark.common.prefab;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
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

    public Pair<Integer, Integer> getBlockPair(World world)
    {
        return new Pair<Integer, Integer>(this.getBlockID(world), this.getBlockMeta(world));
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
        return Math.sqrt(Math.sqrt(deltaX * deltaX + deltaY * deltaY) + deltaZ * deltaZ);
    }
    public double getDistanceFrom2D(Pos pos)
    {
        double deltaX = xx - pos.xx;
        double deltaZ = zz - pos.zz;
        return deltaX * deltaX  + deltaZ * deltaZ;
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getBlockTileEntity(x(), y(), z());
    }

    public AxisAlignedBB expandBound(Pos end)
    {
        return AxisAlignedBB.getBoundingBox(xx - .1, yy - .1, zz - .1, xx + .1, yy + .1, zz + .1).expand(end.xx, end.yy, end.zz);
    }

    public Pos multi(double d)
    {
        this.xx *= d;
        this.yy *= d;
        this.zz *= d;
        return this;

    }

    public Pos div(double d)
    {
        if (d != 0)
        {
            this.xx /= d;
            this.yy /= d;
            this.zz /= d;
        }
        return this;
    }

    public Pos div(Pos d)
    {
        if (d.xx != 0)
        {
            this.xx /= d.xx;
        }
        if (d.yy != 0)
        {
            this.yy /= d.yy;
        }
        if (d.zz != 0)
        {
            this.zz /= d.zz;
        }

        return this;
    }

    public Pos add(Pos center)
    {
        xx += center.xx;
        yy += center.yy;
        zz += center.zz;
        return this;
    }

    public Pos sub(Pos center)
    {
        xx -= center.xx;
        yy -= center.yy;
        zz -= center.zz;
        return this;
    }

    @Override
    public String toString()
    {
        return x() + "X " + y() + "Y " + z() + "Z ";
    }

    @Override
    public boolean equals(Object paramObject)
    {
        if (paramObject instanceof Pos)
        {
            Pos pos = (Pos) paramObject;
            return pos.xx == this.xx && pos.yy == this.yy && pos.zz == this.zz;
        }
        return false;
    }

    public NBTTagCompound save(NBTTagCompound tag)
    {
        tag.setDouble("xx", xx);
        tag.setDouble("yy", yy);
        tag.setDouble("zz", zz);
        return tag;
    }

    public Pos load(NBTTagCompound tag)
    {
        this.xx = tag.getDouble("xx");
        this.yy = tag.getDouble("yy");
        this.zz = tag.getDouble("zz");
        return this;
    }

}
