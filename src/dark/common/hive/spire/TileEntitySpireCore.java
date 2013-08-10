package dark.common.hive.spire;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.hive.TileHive;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class TileEntitySpireCore extends TileHive
{
    HiveSpire spire;
    private int size;

    @Override
    public void init()
    {
        if (this.getSpire().size < this.size)
        {
            this.getSpire().size = this.size;
        }
        //this.getSpire().init();
        if (this.getSpire() != null)
        {
            System.out.println("Spire activated at " + this.getSpire().getLocation().toString());
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % mcday == 0)
            {
                //this.getSpire().scanArea();
            }

            if (this.ticks % 5 == 0)
            {
                List<Entity> list = this.worldObj.playerEntities;
                if (list != null)
                {
                    for (Entity entity : list)
                    {
                        if (entity instanceof EntityPlayer)
                        {
                            //this.getSpire().triggerTrapIfNear(this, (EntityPlayer) entity);
                        }
                    }
                }
            }
        }

    }

    public HiveSpire getSpire()
    {
        if (spire == null)
        {
            spire = HiveSpire.getSpire(new PosWorld(this.worldObj, new Pos(this)), 3);
            if (spire == null)
            {
                spire = new HiveSpire(this);
                spire.init();
            }
        }
        return spire;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 1, this.yCoord + 4, this.zCoord + 1);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.size = nbt.getInteger("Size");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("Size", this.size);
    }

}
