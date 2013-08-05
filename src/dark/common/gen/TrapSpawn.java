package dark.common.gen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;
import dark.common.prefab.Pos;
import dark.common.prefab.Trap;

public class TrapSpawn extends Trap
{
    boolean canTrigger = true;

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

    @Override
    public boolean triggerTrap(World world)
    {
        canTrigger = false;
        for (int i = 0; i < world.rand.nextInt(4); i++)
        {
            double xx = pos.xx + world.rand.nextFloat();
            double yy = pos.yy + world.rand.nextFloat();
            double zz = pos.zz + world.rand.nextFloat();

            if (world.isRemote)
            {
                world.spawnParticle("smoke", xx, yy, zz, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", xx, yy, zz, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                EntityZombie entity = new EntityZombie(world);
                entity.setPosition(xx, yy, zz);
            }

        }
        return true;
    }

    @Override
    public void reset(World world)
    {
        this.canTrigger = true;
    }
}
