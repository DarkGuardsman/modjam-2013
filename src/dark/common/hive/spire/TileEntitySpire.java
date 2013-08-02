package dark.common.hive.spire;

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
        System.out.print("Sleep mode decatived for spire at " + xCoord + "x " + yCoord + "y " + zCoord + "z ");
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
}
