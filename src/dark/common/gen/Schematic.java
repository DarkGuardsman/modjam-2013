package dark.common.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import dark.common.DarkBotMain;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

import net.minecraft.block.Block;
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
            NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(new FileInputStream(new File(file, fileName + ".schematic")));

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
            for (int index = 0; index < blockID.length; index++)
            {

                if ((index >> 1) >= addId.length)
                {
                    System.out.println("BlockID: " + blockID[index]);
                    blocks[index] = (short) (blockID[index] & 0xFF);
                }
                else
                {
                    System.out.println("BlockID: " + blockID[index] + " AddID: " + addId[index]);
                    if ((index & 1) == 0)
                    {
                        blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockID[index] & 0xFF));
                    }
                    else
                    {
                        blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockID[index] & 0xFF));
                    }
                }
                if (blocks[index] != 0)
                {
                    System.out.println("BlockAdd: " + blocks[index]);
                }
            }
            //We don't need these right now but might include them later
            //NBTTagList entities = nbtdata.getTagList("Entities");
            //NBTTagList tileentities = nbtdata.getTagList("TileEntities");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public void build(PosWorld location, boolean air)
    {
        Pos start = new Pos(location.xx, location.yy, location.zz);
        Pos end = new Pos(location.xx + width, Math.min(location.yy + height, 255), location.zz + length);

        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                for (int z = 0; z < length; ++z)
                {
                    int i = y * width * length + z * width + x;
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
                    if (b == 2000)
                    {
                        b = DarkBotMain.blockDeco.blockID;
                    }
                    if (Block.blocksList[b] == null && b != 0)
                    {
                        //System.out.println("Missing Block: " + b);
                        b = 0;
                    }
                    // System.out.println("Placing: " + b + "  " + m);
                    //location.world.setBlock(x + start.x(), y + start.y(), z + start.z(), 0, 0, 2);
                    if (air && b != 0 || !air)
                    {
                        location.world.setBlock(x + start.x(), y + start.y(), z + start.z(), b, m, 2);
                    }
                }
            }
        }
    }
}
