package dark.common.gen;

import net.minecraft.entity.Entity;
import dark.common.prefab.Pos;
import dark.common.prefab.Trap;

public class TrapFall extends Trap
{

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

    public void triggerTrap()
    {

    }
}
