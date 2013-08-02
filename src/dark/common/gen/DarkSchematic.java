package dark.common.gen;

import java.util.HashMap;

import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import dark.common.prefab.Pos;

public class DarkSchematic
{
    public HashMap<Pos, Pair<Integer, Integer>> blocks = new HashMap<>();

    public void loadWorldSelection(World world, Pos pos, Pos pos2)
    {
        int deltaX, deltaY, deltaZ;
        Pos start;
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

    }
}
