package dark.common.prefab;

import net.minecraft.block.Block;

public class BlockWrapper
{
    public Block block;
    public PosWorld pos;

    public BlockWrapper(Block block, PosWorld pos)
    {
        this.block = block;
        this.pos = pos;
    }
}
