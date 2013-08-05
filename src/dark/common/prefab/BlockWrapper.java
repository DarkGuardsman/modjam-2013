package dark.common.prefab;

import net.minecraft.block.Block;

public class BlockWrapper
{
    Block block;
    PosWorld pos;

    public BlockWrapper(Block block, PosWorld pos)
    {
        this.block = block;
        this.pos = pos;
    }
}
