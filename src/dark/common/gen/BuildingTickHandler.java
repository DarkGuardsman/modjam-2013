package dark.common.gen;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.Trap;

public class BuildingTickHandler implements ITickHandler
{

    static HashMap<Trap, Integer> trapResetList = new HashMap<Trap, Integer>();
    static HashMap<Trap, HiveSpire> retrunList = new HashMap<Trap, HiveSpire>();

    public static void markTrapReturn(HiveSpire hive, Trap trap, int ticks)
    {
        trapResetList.put(trap, ticks);
        retrunList.put(trap, hive);
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        Iterator<Entry<Trap, Integer>> it = trapResetList.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<Trap, Integer> entry = it.next();
            int tick = entry.getValue();
            if (tick-- <= 0)
            {
                HiveSpire hive = retrunList.get(entry.getKey());
                if (hive != null)
                {
                    hive.loadedTraps.add(entry.getKey());
                    entry.getKey().reset(hive.getLocation().world);
                }
                trapResetList.remove(entry.getKey());
            }
            else
            {
                trapResetList.put(entry.getKey(), entry.getValue() - 1);
            }

        }

    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel()
    {
        return "Building-TickHandler";
    }

}
