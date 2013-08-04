package dark.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.event.world.WorldEvent.Save;

public class SaveManger
{
    public static List<IOnSave> saveClass = new ArrayList<IOnSave>();

    public void registerClass(IOnSave clazz)
    {
        if (!saveClass.contains(clazz))
        {
            saveClass.add(clazz);
        }
    }

    public void worldSaveEvent(Save event)
    {

    }
}
