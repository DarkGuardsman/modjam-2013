package dark.common.gen;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import dark.common.entity.EntityDefender;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;
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
        for (int i = 0; i < 1 + world.rand.nextInt(5); i++)
        {

            double xx = pos.xx + world.rand.nextInt(5);
            double yy = pos.yy + world.rand.nextInt(2);
            double zz = pos.zz + world.rand.nextInt(5);
            PosWorld pos = new PosWorld(world, xx, yy, zz);
            if (pos.getBlockID() == 0 && pos.add(new Pos(0, 1, 0)).getBlockID(world) == 0 && pos.sub(new Pos(0, 2, 0)).getBlockID(world) != 0)
            {
                if (world.isRemote)
                {
                    world.spawnParticle("smoke", xx, yy, zz, 0.0D, 0.0D, 0.0D);
                    world.spawnParticle("flame", xx, yy, zz, 0.0D, 0.0D, 0.0D);
                }
                else
                {
                    EntityDefender entity = new EntityDefender(world);
                    entity.setPosition(xx, yy, zz);
                    world.spawnEntityInWorld(entity);
                    entity.playLivingSound();
                }
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
