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
    short[] blocks;
    byte[] data;

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
            fis.close();
            width = nbtdata.getShort("Width");
            height = nbtdata.getShort("Height");
            length = nbtdata.getShort("Length");

            byte[] blockID = nbtdata.getByteArray("Blocks");
            this.data = nbtdata.getByteArray("Data");
            byte[] addId = new byte[0];
            this.blocks = new short[blockID.length];

            if (nbtdata.hasKey("AddBlocks"))
            {
                addId = nbtdata.getByteArray("AddBlocks");
            }

            // Combine the AddBlocks data with the first 8-bit block ID
            for (int i = 0; i < blockID.length; i++)
            {
                if ((i >> 1) >= addId.length)
                {
                    this.blocks[i] = (short) (blockID[i] & 0xFF);
                }
                else
                {
                    if ((i & 1) == 0)
                    {
                        this.blocks[i] = (short) (((addId[i >> 1] & 0x0F) << 8) + (blockID[i] & 0xFF));
                    }
                    else
                    {
                        this.blocks[i] = (short) (((addId[i >> 1] & 0xF0) << 4) + (blockID[i] & 0xFF));
                    }
                }
            }
            for (int i = 0; i < blocks.length; i++)
            {
                //System.out.println("Block: "+blocks[i]);
            }
            //NBTTagList entities = nbtdata.getTagList("Entities");
            //NBTTagList tileentities = nbtdata.getTagList("TileEntities");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public void build(PosWorld location)
    {
        Pos start = new Pos(location.xx, location.yy, location.zz);
        Pos end = new Pos(location.xx + width, Math.min(location.yy + height, 255), location.zz + length);

        for (int x = start.x(); x < end.x(); x++)
        {
            for (int y = start.y(); y < end.y(); y++)
            {
                for (int z = start.z(); z < end.z(); z++)
                {
                    int i = (y - start.y()) * width * length + (z - start.z()) * width + (x - start.x());
                    int b = 0;
                    int m = 0;
                    if (i < this.blocks.length)
                    {
                        b = this.blocks[i];
                    }
                    if (i < this.data.length)
                    {
                        m = this.data[i];
                    }
                    System.out.println("Placing: " + b + "  " + m);
                    location.world.setBlock(x, y, z, b, m, 2);
                }
            }
        }
    }
}
