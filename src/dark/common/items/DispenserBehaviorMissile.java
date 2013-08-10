package dark.common.items;

import dark.common.entity.EntityBombMissile;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public final class DispenserBehaviorMissile extends BehaviorProjectileDispense
{
    /** Return the projectile entity spawned by this dispense behavior. */
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        return new EntityBombMissile(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
    }
}
