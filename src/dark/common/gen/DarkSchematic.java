package dark.common.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;

public class DarkSchematic
{
    public HashMap<Pos, Pair<Integer, Integer>> blocks = new HashMap<>();

    String fileName;

    public DarkSchematic(String fileName)
    {
        fileName = fileName;
    }

    public void loadWorldSelection(World world, Pos pos, Pos pos2)
    {
        int deltaX, deltaY, deltaZ;
        Pos start = new Pos(pos.xx > pos2.xx ? pos2.xx : pos.xx, pos.yy > pos2.yy ? pos2.yy : pos.yy, pos.zz > pos2.zz ? pos2.zz : pos.zz);
        if (pos.xx < pos2.xx)
        {
            deltaX = pos2.x() - pos.x();
        }
        else
        {
            deltaX = pos.x() - pos2.x();
        }
        if (pos.yy < pos2.yy)
        {
            deltaY = pos2.y() - pos.y();
        }
        else
        {
            deltaY = pos.y() - pos2.y();
        }
        if (pos.zz < pos2.zz)
        {
            deltaZ = pos2.z() - pos.z();
        }
        else
        {
            deltaZ = pos.z() - pos2.z();
        }
        for (int x = 0; x < deltaX; ++x)
        {
            for (int y = 0; y < deltaY; ++y)
            {
                for (int z = 0; z < deltaZ; ++z)
                {
                    int blockID = world.getBlockId(start.x() + x, start.y() + y, start.z() + z);
                    int blockMeta = world.getBlockMetadata(start.x() + x, start.y() + y, start.z() + z);
                    blocks.put(new Pos(x, y, z), new Pair<Integer, Integer>(blockID, blockMeta));
                }
            }
        }

    }

    public DarkSchematic load()
    {
        try
        {
            File file = new File(McEditSchematic.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
            NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(new FileInputStream(new File(file, fileName + ".sch")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public void save()
    {
        try
        {
            File file = new File(McEditSchematic.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
            NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(new FileInputStream(new File(file, fileName + ".sch")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
