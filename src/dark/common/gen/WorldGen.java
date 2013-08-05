package dark.common.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import dark.common.hive.spire.HiveSpire;
import dark.common.prefab.PosWorld;

public class WorldGen extends WorldGenerator implements IWorldGenerator
{

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        chunkX = chunkX << 4;
        chunkZ = chunkZ << 4;
        PosWorld pos = new PosWorld(world, chunkX, 63,chunkZ);
        if(random.nextInt(5) == 1 && HiveSpire.getSpire(pos, 100) == null);

    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        HiveSpire spire = new HiveSpire(new PosWorld(world, i, j, k));
        HiveSpire.buildSpire(spire, 1 + random.nextInt(HiveSpire.MAX_SIZE - 1));
        return true;
    }

}
