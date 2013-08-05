package dark.common.gen;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.Trap;

public class TrapFall extends Trap
{
    Pair<Integer,Integer> resetBlock = null;
    public TrapFall(Pos pos)
    {
        super(pos, "fall", 10);
    }

    @Override
    public boolean canTrigger(Entity entity, Pos pos)
    {
        if (entity != null && pos != null)
        {
            return new Pos(entity).getDistanceFrom(this.pos) < 2;
        }
        return false;
    }

    @Override
    public boolean triggerTrap(World world)
    {
        System.out.println("Trap triggered " + type);
        pos.setBlock(world, 0);
        return true;
    }

    public void reset()
    {

    }
}
