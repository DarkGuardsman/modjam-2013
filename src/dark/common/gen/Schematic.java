package dark.common.gen;

import java.io.File;
import java.io.InputStream;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Schematic
{
    String fileName;
    short width,height,length;
    byte[] blocks,data;

    public Schematic(String fileName)
    {

    }

    public Schematic load()
    {

        try
        {
            InputStream fis = getClass().getResourceAsStream(fileName + ".schematic");
            NBTTagCompound nbtdata = NBTCompressedStreamTools(fis);

            width = nbtdata.getShort("Width");
            height = nbtdata.getShort("Height");
            length = nbtdata.getShort("Length");

            blocks = nbtdata.getByteArray("Blocks");
            data = nbtdata.getByteArray("Data");

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
}
