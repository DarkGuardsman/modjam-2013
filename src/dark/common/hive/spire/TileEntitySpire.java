package dark.common.hive.spire;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
    HashMap<Trap, Pair<Integer, Pair<Integer, Integer>>> trapResetList = new HashMap<Trap, Pair<Integer, Pair<Integer, Integer>>>();

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
            Iterator<Entry<Trap, Pair<Integer, Pair<Integer, Integer>>>> it = this.trapResetList.entrySet().iterator();
            while (it.hasNext())
            {
                Entry<Trap, Pair<Integer, Pair<Integer, Integer>>> entry = it.next();
                int tick = entry.getValue().getOne();
                if (tick-- <= 0)
                {
                    this.getSpire().loadedTraps.add(entry.getKey());
                    entry.getKey().pos.setBlock(worldObj, entry.getValue().getTwo().getOne(), entry.getValue().getTwo().getTwo());
                    this.trapResetList.remove(entry.getKey());
                }
                else
                {
                    this.trapResetList.put(entry.getKey(), new Pair<Integer, Pair<Integer, Integer>>(entry.getValue().getOne() - 1, entry.getValue().getTwo()));
                }

            }
            if (this.ticks % 5 == 0)
            {
                List<Entity> list = this.getSpire().getEntitiesInRange();
                if (list != null)
                {
                    System.out.println("Entity Count in range of spire " + list.size());
                    for (Entity entity : list)
                    {
                        Pos pos = new Pos(entity);
                        if (entity instanceof EntityPlayer)
                        {
                            this.getSpire().triggerTrapIfNear(this, (EntityPlayer) entity);
                        }
                    }
                }
            }
        }

    }

    public void markTrapReturn(Trap trap, int ticks, Pair<Integer, Integer> block)
    {
        this.trapResetList.put(trap, new Pair<Integer, Pair<Integer, Integer>>(ticks, block));

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

}
