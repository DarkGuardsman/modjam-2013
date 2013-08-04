package dark.common.gen;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import dark.common.DarkBotMain;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

/** Schematic system that is only used for creating world gen structures for this mod
 *
 * @author DarkGuardsman */
public class DarkSchematic
{
    public static final String BlockList = "BlockList";
    public static boolean mapSet = false;

    public static HashMap<Integer, Pair<String, Integer>> pathBlockMap = new HashMap<Integer, Pair<String, Integer>>();
    public static HashMap<String, Integer> blockChangeIDs = new HashMap<String, Integer>();
    public static HashMap<Integer, String> blockChangeIDReverse = new HashMap<Integer, String>();

    public HashMap<Pos, Pair<Integer, Integer>> blocks = new HashMap<Pos, Pair<Integer, Integer>>();
    public NBTTagCompound extraData = new NBTTagCompound();
    public Pos center;
    public Pos size;
    public String fileName;

    public DarkSchematic(String fileName)
    {
        this.fileName = fileName;
        if (!mapSet)
        {
            pathBlockMap.put(1, new Pair<String, Integer>("AA", Block.oreGold.blockID));
            pathBlockMap.put(2, new Pair<String, Integer>("AB", Block.oreLapis.blockID));
            blockChangeIDs.put("B", DarkBotMain.blockDeco.blockID);
            blockChangeIDReverse.put(DarkBotMain.blockDeco.blockID, "B");
            blockChangeIDs.put("C", DarkBotMain.blockCore.blockID);
            blockChangeIDReverse.put(DarkBotMain.blockCore.blockID, "C");
        }
    }

    public Pos getCenter()
    {
        if (this.center == null && size != null)
        {
            this.center = this.size.clone().multi(.5);
        }
        if (this.center == null)
        {
            this.center = new Pos();
        }
        return center;
    }

    public DarkSchematic loadWorldSelection(World world, Pos pos, Pos pos2)
    {
        int deltaX, deltaY, deltaZ;
        Pos start = new Pos(pos.xx > pos2.xx ? pos2.xx : pos.xx, pos.yy > pos2.yy ? pos2.yy : pos.yy, pos.zz > pos2.zz ? pos2.zz : pos.zz);
        this.center = new Pos();
        if (pos.xx < pos2.xx)
        {
            deltaX = pos2.x() - pos.x() + 1;
        }
        else
        {
            deltaX = pos.x() - pos2.x() + 1;
        }
        if (pos.yy < pos2.yy)
        {
            deltaY = pos2.y() - pos.y() + 1;
        }
        else
        {
            deltaY = pos.y() - pos2.y() + 1;
        }
        if (pos.zz < pos2.zz)
        {
            deltaZ = pos2.z() - pos.z() + 1;
        }
        else
        {
            deltaZ = pos.z() - pos2.z() + 1;
        }
        this.size = new Pos(deltaX, deltaY, deltaZ);
        for (int x = 0; x < deltaX; ++x)
        {
            for (int y = 0; y < deltaY; ++y)
            {
                for (int z = 0; z < deltaZ; ++z)
                {
                    int blockID = world.getBlockId(start.x() + x, start.y() + y, start.z() + z);
                    int blockMeta = world.getBlockMetadata(start.x() + x, start.y() + y, start.z() + z);
                    if (blockID == DarkBotMain.blockCore.blockID)
                    {
                        this.center = new Pos(x, y, z);
                    }
                    if (blockID == Block.sponge.blockID)
                    {
                        blockID = 0;
                        blockMeta = 0;
                    }
                    if (blockID == Block.blockDiamond.blockID)
                    {
                        blockID = DarkBotMain.blockDeco.blockID;
                        blockMeta = 0;
                        this.defineTrap(0, new Pos(x, y, z));
                    }
                    blocks.put(new Pos(x, y, z), new Pair<Integer, Integer>(blockID, blockMeta));
                }
            }
        }
        return this;
    }

    public DarkSchematic load()
    {
        try
        {
            File file = new File(McEditSchematic.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
            NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(new FileInputStream(new File(file, fileName + ".dat")));
            size = new Pos(nbtdata.getInteger("sizeX"), nbtdata.getInteger("sizeY"), nbtdata.getInteger("sizeZ"));
            center = new Pos(nbtdata.getInteger("centerX"), nbtdata.getInteger("centerY"), nbtdata.getInteger("centerZ"));
            this.extraData = nbtdata.getCompoundTag("extradata");
            NBTTagCompound blockSet = nbtdata.getCompoundTag(BlockList);
            for (int i = 0; i < blockSet.getInteger("count"); i++)
            {
                String output = blockSet.getString("Block" + i);
                String[] out = output.split(":");
                int b = 0;
                int m = 0;
                Pos pos = new Pos();
                if (out != null)
                {
                    try
                    {
                        boolean flag = false;
                        if (out.length > 0)
                        {
                            if (blockChangeIDs.containsKey(out[0]))
                            {
                                b = blockChangeIDs.get(out[0]);
                            }
                            else
                            {
                                b = Integer.parseInt(out[0]);
                            }
                        }
                        if (out.length > 1)
                        {
                            m = Integer.parseInt(out[1]);
                        }
                        if (out.length > 2)
                        {
                            pos.xx = Integer.parseInt(out[2]);
                        }
                        if (out.length > 3)
                        {
                            pos.yy = Integer.parseInt(out[3]);
                        }
                        if (out.length > 4)
                        {
                            pos.zz = Integer.parseInt(out[4]);
                        }
                        if (flag)
                        {
                            this.center = pos;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    this.blocks.put(pos, new Pair<Integer, Integer>(b, m));
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public DarkSchematic save()
    {
        try
        {
            int sudoID = Block.sponge.blockID;

            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setCompoundTag("extradata", this.extraData);
            NBTTagCompound blockNBT = nbt.getCompoundTag(BlockList);
            nbt.setInteger("sizeX", size.x());
            nbt.setInteger("sizeY", size.y());
            nbt.setInteger("sizeZ", size.z());
            nbt.setInteger("centerX", center.x());
            nbt.setInteger("centerY", center.y());
            nbt.setInteger("centerZ", center.z());
            int i = 0;

            for (Entry<Pos, Pair<Integer, Integer>> entry : blocks.entrySet())
            {
                String output = "";
                String block = "" + entry.getValue().getOne();
                if (this.blockChangeIDReverse.containsKey(entry.getValue().getOne()))
                {
                    block = blockChangeIDReverse.get(entry.getValue().getOne());
                }
                output += block;
                output += ":" + entry.getValue().getTwo();
                output += ":" + entry.getKey().x() + ":" + entry.getKey().y() + ":" + entry.getKey().z();
                blockNBT.setString("Block" + i, output);
                i++;
            }
            blockNBT.setInteger("count", i);
            nbt.setCompoundTag(BlockList, blockNBT);

            NBTFileSaver.saveNBTFile(fileName + ".dat", NBTFileSaver.getFolder("schematics", true), nbt, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public static void buildNormal(PosWorld posWorld, Pos offset, boolean replaceAir, HashMap<Pos, Pair<Integer, Integer>> placementMap, Pos... ignoredSpots)
    {
        List<Pos> ignore = new ArrayList<Pos>();
        ignore.addAll(Arrays.asList(ignoredSpots));
        if (offset == null)
        {
            offset = new Pos();
        }
        if (placementMap != null && posWorld != null)
            for (Entry<Pos, Pair<Integer, Integer>> entry : placementMap.entrySet())
            {
                Pos setPos = new Pos(posWorld.xx - offset.xx + entry.getKey().xx, posWorld.yy - offset.yy + entry.getKey().yy, posWorld.zz - offset.zz + entry.getKey().zz);
                if (entry.getValue().getOne() != 0 && !replaceAir || replaceAir)
                {
                    if (setPos.getTileEntity(posWorld.world) == null && !ignore.contains(setPos))
                    {
                        setPos.setBlock(posWorld.world, entry.getValue().getOne(), entry.getValue().getTwo());
                    }
                }
            }
    }

    /** Builds a normal schematic setup
     *
     * @param posWorld - world location to build the center of the schematic at
     * @param ignoreAir - should we replace blocks with air if the schematic calls for it
     * @param ignore - location to ignore */
    public void build(PosWorld posWorld, boolean replaceAir, Pos... ignore)
    {
        buildNormal(posWorld, this.getCenter(), replaceAir, this.blocks, ignore);
    }

    /** Builds a spire schematic to a set of rules
     *
     * @param posWorld - world location
     * @param ignoreAir - should ignore air blocks in schematic
     * @param center - center the schematic to the core
     * @param path - path var to go with. Will change the schematic behavior
     * @param ignoreList - list of blocks to ignore. generaly the core block */
    public void buildSpire(PosWorld posWorld, boolean ignoreAir, boolean center, int path, Pos... ignoreList)
    {
        System.out.println("Building schematic " + posWorld.toString());
        int pathMark = 0;
        List<Integer> replaceIDs = new ArrayList<Integer>();
        for (Entry<Integer, Pair<String, Integer>> entry : pathBlockMap.entrySet())
        {
            if (entry.getKey() == path)
            {
                pathMark = entry.getValue().getTwo();
            }
            else
            {
                replaceIDs.add(entry.getValue().getTwo());
            }
        }
        HashMap<Pos, Pair<Integer, Integer>> newMap = new HashMap<Pos, Pair<Integer, Integer>>();
        for (Entry<Pos, Pair<Integer, Integer>> entry : blocks.entrySet())
        {
            int blockID = entry.getValue().getOne();
            int meta = entry.getValue().getTwo();

            if (blockID == Block.sponge.blockID || blockID == Block.blockDiamond.blockID)
            {
                blockID = 0;
                meta = 0;
            }
            else if (blockID == pathMark)
            {
                blockID = 0;
                meta = 0;
            }
            else if (replaceIDs.contains(blockID))
            {
                blockID = DarkBotMain.blockDeco.blockID;
                meta = 0;
            }
            newMap.put(entry.getKey(), new Pair<Integer, Integer>(blockID, meta));

        }
        buildNormal(posWorld, this.getCenter(), ignoreAir, newMap, ignoreList);

    }

    public void defineTrap(int type, Pos start)
    {
        String t = null;
        if (type == 0)
        {
            t = "fall";
        }
        if (t != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", t);
            tag.setCompoundTag("start", start.save(new NBTTagCompound()));

            NBTTagCompound trap = this.extraData.getCompoundTag("traps");
            int c = trap.getInteger("count");
            trap.setCompoundTag("trap" + c + 1, tag);
            trap.setInteger("count", c + 1);
            this.extraData.setCompoundTag("traps", trap);
        }

    }
}
