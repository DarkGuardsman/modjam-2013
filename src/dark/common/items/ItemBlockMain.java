package dark.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMain extends ItemBlock
{

    public ItemBlockMain(int id)
    {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + itemStack.getItemDamage();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
