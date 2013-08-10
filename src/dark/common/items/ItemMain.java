package dark.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import dark.common.DarkBotMain;

public class ItemMain extends Item
{
    public ItemMain(int par, String name, CreativeTabs tab)
    {
        super(DarkBotMain.config.getItem(name, par).getInt());
        this.setUnlocalizedName(name);
        this.setCreativeTab(tab);
        this.func_111206_d(DarkBotMain.PREFIX+name);
    }

}
