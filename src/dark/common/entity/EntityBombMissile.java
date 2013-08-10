package dark.common.entity;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBombMissile extends Entity implements IProjectile
{
    private int xTile = -1, yTile = -1, zTile = -1, groundID, groundMeta;
    /** Is this in/on a block */
    private boolean inGround;

    /** The owner of this missile. */
    public Entity shootingEntity;
    private int ticksInGround, ticksInAir;
    private double damage = 2.0D;

    /** The amount of knockback an arrow applies when it hits a mob. */
    private int knockbackStrength;
    /** Delay before the bomb should blow up after inpacting the ground */
    private int tickInGroundDelay;

    public EntityBombMissile(World world)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityBombMissile(World world, double x, double y, double z)
    {
        this(world);
        this.setPosition(x, y, z);
        this.yOffset = 0.0F;
    }

    /** Used the difference in location of the shooter and target to position the missile
     *
     * @param world - world both the shooter and target are in
     * @param shooter - entity that is firing this missile
     * @param target - were the missile is destine for
     * @param velocity - starting velocity of the missile
     * @param spread - how far off center the missile should fly */
    public EntityBombMissile(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity, float spread)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D;
        double deltaX = target.posX - shooter.posX;
        double deltaY = target.boundingBox.minY + (double) (target.height / 3.0F) - this.posY;
        double deltaZ = target.posZ - shooter.posZ;
        double xzMag = (double) MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

        if (xzMag >= 1.0E-7D)
        {
            float yaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float) (-(Math.atan2(deltaY, xzMag) * 180.0D / Math.PI));

            double changeX = deltaX / xzMag;
            double changeZ = deltaZ / xzMag;

            this.setLocationAndAngles(shooter.posX + changeX, this.posY, shooter.posZ + changeZ, yaw, pitch);

            this.yOffset = 0.0F;

            float f4 = (float) xzMag * 0.2F;

            this.setThrowableHeading(deltaX, deltaY + (double) f4, deltaZ, velocity, spread);
        }
    }

    /** Uses the shooters facing direction and velocity to fire the missile */
    public EntityBombMissile(World world, EntityLivingBase shooter, float velocity)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
    }

    @Override
    protected void entityInit()
    {
    }

    /** Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction. */
    public void setThrowableHeading(double xx, double yy, double zz, float velocity, float spread)
    {
        float mag = MathHelper.sqrt_double(xx * xx + yy * yy + zz * zz);
        xx /= (double) mag;
        yy /= (double) mag;
        zz /= (double) mag;
        xx += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) spread;
        yy += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) spread;
        zz += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) spread;
        xx *= (double) velocity;
        yy *= (double) velocity;
        zz *= (double) velocity;
        this.motionX = xx;
        this.motionY = yy;
        this.motionZ = zz;
        float xzMag = MathHelper.sqrt_double(xx * xx + zz * zz);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(xx, zz) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(yy, (double) xzMag) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    public void setPositionAndRotation2(double xx, double yy, double zz, float yaw, float pitch, int par9)
    {
        this.setPosition(xx, yy, zz);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void setVelocity(double vx, double vy, double vz)
    {
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(vx * vx + vz * vz);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(vx, vz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(vy, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float xzMag = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) xzMag) * 180.0D / Math.PI);
        }

        int i = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

        if (i > 0)
        {
            Block.blocksList[i].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (axisalignedbb != null && axisalignedbb.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.inGround)
        {
            int blockID = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int blockMeta = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if (blockID == this.groundID && blockMeta == this.groundMeta)
            {
                if (ticksInGround == 0)
                {
                    this.tickInGroundDelay = 10 + this.worldObj.rand.nextInt(50);
                }
                ++this.ticksInGround;

                if (this.ticksInGround == this.tickInGroundDelay)
                {
                    this.setDead();
                    this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1 + this.worldObj.rand.nextInt(2), false);
                }
            }
            else
            {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3 currentLocation = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 nextLocation = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks_do_do(currentLocation, nextLocation, false, true);
            currentLocation = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            nextLocation = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                nextLocation = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List entityList = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            float f1;

            for (int l = 0; l < entityList.size(); ++l)
            {
                Entity currentEntity = (Entity) entityList.get(l);

                if (currentEntity.canBeCollidedWith() && (currentEntity != this.shootingEntity || this.ticksInAir >= 5))
                {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = currentEntity.boundingBox.expand((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(currentLocation, nextLocation);

                    if (movingobjectposition1 != null)
                    {
                        double distanceTo = currentLocation.distanceTo(movingobjectposition1.hitVec);

                        if (distanceTo < d0 || d0 == 0.0D)
                        {
                            entity = currentEntity;
                            d0 = distanceTo;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).func_96122_a(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f3;

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit != null)
                {
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int i1 = MathHelper.ceiling_double_int((double) f2 * this.damage);

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null)
                    {
                        damagesource = new EntityDamageSourceIndirect("arrow", this, this).setProjectile();
                    }
                    else
                    {
                        damagesource = new EntityDamageSourceIndirect("arrow", this, this.shootingEntity).setProjectile();
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) i1))
                    {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase)
                        {
                            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1 + this.worldObj.rand.nextInt(2), false);
                            EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                            if (!this.worldObj.isRemote)
                            {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0)
                            {
                                f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (f3 > 0.0F)
                                {
                                    movingobjectposition.entityHit.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) f3, 0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) f3);
                                }
                            }

                            if (this.shootingEntity != null)
                            {
                                EnchantmentThorns.func_92096_a(this.shootingEntity, entitylivingbase, this.rand);
                            }

                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                            {
                                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                            }
                            this.setDead();
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }
                    else
                    {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
                else
                {
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.groundID = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.groundMeta = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;

                    if (this.groundID != 0)
                    {
                        Block.blocksList[this.groundID].onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f4 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                for (int j1 = 0; j1 < 4; ++j1)
                {
                    f3 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f3, this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX, this.motionY, this.motionZ);
                }

                f4 = 0.8F;
            }

            this.motionX *= (double) f4;
            this.motionY *= (double) f4;
            this.motionZ *= (double) f4;
            this.motionY -= (double) f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("xTile", (short) this.xTile);
        par1NBTTagCompound.setShort("yTile", (short) this.yTile);
        par1NBTTagCompound.setShort("zTile", (short) this.zTile);
        par1NBTTagCompound.setByte("inTile", (byte) this.groundID);
        par1NBTTagCompound.setByte("inData", (byte) this.groundMeta);
        par1NBTTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        par1NBTTagCompound.setDouble("damage", this.damage);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.xTile = par1NBTTagCompound.getShort("xTile");
        this.yTile = par1NBTTagCompound.getShort("yTile");
        this.zTile = par1NBTTagCompound.getShort("zTile");
        this.groundID = par1NBTTagCompound.getByte("inTile") & 255;
        this.groundMeta = par1NBTTagCompound.getByte("inData") & 255;
        this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

        if (par1NBTTagCompound.hasKey("damage"))
        {
            this.damage = par1NBTTagCompound.getDouble("damage");
        }
    }

    /** returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for
     * spiders and wolves to prevent them from trampling crops */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setDamage(double par1)
    {
        this.damage = par1;
    }

    public double getDamage()
    {
        return this.damage;
    }

    /** Sets the amount of knockback the arrow applies when it hits a mob. */
    public void setKnockbackStrength(int par1)
    {
        this.knockbackStrength = par1;
    }

    /** If returns false, the item will not inflict any damage against entities. */
    public boolean canAttackWithItem()
    {
        return false;
    }
}
