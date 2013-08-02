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
    short[] blocks = new short[1];
    byte[] data = new byte[1];

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

            byte[] blockID = nbtdata.getByteArray("Blocks");
            data = nbtdata.getByteArray("Data");
            byte[] addId = new byte[0];
            int l = blocks.length;
            short[] blocks = new short[l];

            if (nbtdata.hasKey("AddBlocks"))
            {
                addId = nbtdata.getByteArray("AddBlocks");
            }

            // Combine the AddBlocks data with the first 8-bit block ID
            for (int i = 0; i < blocks.length; i++)
            {
                if ((i >> 1) >= addId.length)
                {
                    blocks[i] = (short) (blockID[i] & 0xFF);
                }
                else
                {
                    if ((i & 1) == 0)
                    {
                        blocks[i] = (short) (((addId[i >> 1] & 0x0F) << 8) + (blockID[i] & 0xFF));
                    }
                    else
                    {
                        blocks[i] = (short) (((addId[i >> 1] & 0xF0) << 4) + (blockID[i] & 0xFF));
                    }
                }
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
        Pos start = new Pos(location.xx + (width / 2), Math.min(location.yy + height, 255), location.zz + (length / 2));
        Pos end = new Pos(location.xx - (width / 2), Math.max(location.yy, 0), location.zz - (length / 2));
        int i = 0;
        int x, y, z;
        for (y = start.y(); y <= start.y() && y >= end.y(); y--)
        {
            for (x = start.x(); x <= start.x() && x >= end.x(); x--)
            {
                for (z = start.z(); z <= start.z() && z >= end.z(); z--)
                {
                    i = y * width * length + z * width + x;
                    int b = 0;
                    int m = 0;
                    if (i < blocks.length)
                    {
                        b = blocks[i];
                    }
                    if (i < data.length)
                    {
                        m = data[i];
                    }
                    location.world.setBlock(x, y, z, b, m, 2);
                }
            }
        }
    }
}
