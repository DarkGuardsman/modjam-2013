package dark.common.prefab;

import net.minecraft.block.Block;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMain extends TileEntity
{
    protected Pos pos;
    protected long ticks;

    public void init()
    {

    }

    @Override
    public void updateEntity()
    {
        if (ticks == 0)
        {
            this.init();
        }
        ticks++;
        if (ticks >= Long.MAX_VALUE - 10)
        {
            ticks = 1;
        }
    }

    public Pos getPosition()
    {
        if (pos == null || pos.xx != this.xCoord || pos.yy != this.yCoord || pos.yy != this.yCoord)
        {
            pos = new Pos(this);
        }
        return pos;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return null;
    }

    @Override
    public int getBlockMetadata()
    {
        return this.getPosition().getBlockMeta(this.worldObj);
    }

    public int getBlockID()
    {
        return this.getPosition().getBlockID(this.worldObj);
    }

    @Override
    public Block getBlockType()
    {
        return Block.blocksList[this.getBlockID()];
    }
}
