package dark.common.gen;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class BuildingTickHandler implements ITickHandler
{

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        // TODO Auto-generated method stub

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
