package dark.common.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.PosWorld;

public class WorldGen implements IWorldGenerator
{

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        chunkX = chunkX << 4;
        chunkZ = chunkZ << 4;
        PosWorld pos = new PosWorld(world, chunkX, 63, chunkZ);
        if (random.nextInt(30) == 1 && HiveSpire.getSpire(pos, 400) == null)
        {
            HiveSpire spire = new HiveSpire(pos);
            HiveSpire.buildSpire(spire, 1 + random.nextInt(HiveSpire.MAX_SIZE - 1));
        }

    }

}
