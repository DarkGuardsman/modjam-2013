package dark.common.hive.spire;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;
import dark.common.prefab.TileEntityMain;
import dark.common.prefab.Trap;

public class TileEntitySpire extends TileEntityMain
{
    HiveSpire spire;

    @Override
    public void init()
    {
        this.getSpire().init();
        //TODO get dimension id
        System.out.println("Sleep mode decatived for spire at " + xCoord + "x " + yCoord + "y " + zCoord + "z ");
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % mcday == 0)
            {
                this.getSpire().scanArea();
            }

            if (this.ticks % 5 == 0)
            {
                List<Entity> list = this.getSpire().getEntitiesInRange();
                if (list != null)
                {
                    for (Entity entity : list)
                    {
                        if (entity instanceof EntityPlayer)
                        {
                            this.getSpire().triggerTrapIfNear(this, (EntityPlayer) entity);
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
            spire = new HiveSpire(this);
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
        if(this.getSpire() != null)
        {
            this.getSpire().loadSpire(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if(this.getSpire() != null)
        {
            this.getSpire().saveSpire(nbt);
        }
    }

}
