package dark.common.hive.spire;

import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;
import dark.common.prefab.TileEntityMain;

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
        if (this.ticks % mcday == 0)
        {
            this.getSpire().scanArea();
        }

    }

    public HiveSpire getSpire()
    {
        if (spire == null)
        {
            spire = HiveSpire.getSpire(new PosWorld(this.worldObj, new Pos(this)),3);
            spire = new HiveSpire(this);
        }
        return spire;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord-1, this.yCoord, this.zCoord-1, this.xCoord + 1, this.yCoord + 4, this.zCoord + 1);
    }
}
