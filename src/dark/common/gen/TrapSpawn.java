package dark.common.gen;

import net.minecraft.entity.Entity;
import dark.common.prefab.Pos;
import dark.common.prefab.Trap;

public class TrapSpawn extends Trap
{

    public TrapSpawn(Pos pos)
    {
        super(pos, "spawn", 120);
    }

    @Override
    public boolean canTrigger(Entity entity, Pos pos)
    {
        if (entity != null && pos != null)
        {
            return new Pos(entity).getDistanceFrom(this.pos) < 20;
        }
        return false;
    }

    public void triggerTrap()
    {

    }
}
