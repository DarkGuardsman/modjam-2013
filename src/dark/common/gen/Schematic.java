package dark.common.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import dark.common.prefab.PosWorld;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class Schematic
{
    String fileName;
    short width, height, length;
    byte[] blocks, data;

    public Schematic(String fileName)
    {
        this.fileName = fileName;
    }

    public Schematic load()
    {

        try
        {
            ResourceLocation location;
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
                System.out.println("BlockByte" + i + ":" + blocks[i]);
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

    }
}
