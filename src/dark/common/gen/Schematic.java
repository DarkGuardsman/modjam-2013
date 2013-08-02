package dark.common.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class Schematic
{
    String fileName;
    short width;
    public short height;
    short length;
    byte[] blocks, data;

    public Schematic(String fileName)
    {
        this.fileName = fileName;
    }

    public Schematic load()
    {

        try
        {
            File file = new File(Schematic.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
            InputStream fis = new FileInputStream(file.getPath() + File.separator + "fireflower.schematic");
            NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(fis);

            width = nbtdata.getShort("Width");
            height = nbtdata.getShort("Height");
            length = nbtdata.getShort("Length");

            blocks = nbtdata.getByteArray("Blocks");
            data = nbtdata.getByteArray("Data");
            for (int i = 0; i < blocks.length; i++)
            {
                System.out.println("BlockByte" + i + ":" + blocks[i] +"  "+data[i]);
            }
            //NBTTagList entities = nbtdata.getTagList("Entities");
            //NBTTagList tileentities = nbtdata.getTagList("TileEntities");

            fis.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public void build(PosWorld location)
    {
        Pos start = new Pos(location.xx + (width/2), Math.min(location.yy + height, 255), location.zz + (length/2));
        Pos end = new Pos(location.xx - (width/2), Math.max(location.yy, 0), location.zz - (length/2));
        int i = 0;
        int x, y, z;
        for (y = start.y(); y <= start.y() && y >= end.y(); y--)
        {
            for (x = start.x(); x <= start.x() && x >= end.x(); x--)
            {
                for (z = start.z(); z <= start.z() && z >= end.z(); z--)
                {
                    i++;
                    int b = 0;
                    int m = 0;
                    if(i < blocks.length)
                    {
                        b = blocks[i];
                    }
                    if(i <data.length)
                    {
                        m = data[i];
                    }
                    location.world.setBlock(x, y, z, b,m,2);
                }
            }
        }
    }
}
