package dark.common.items;

import java.io.File;
import java.net.URL;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import dark.common.DarkBotMain;
import dark.common.gen.Schematic;

public class ItemSpawnTool extends Item
{

    public ItemSpawnTool(int par)
    {
        super(DarkBotMain.config.getItem("SpawnTool", par).getInt());
        this.setUnlocalizedName("SpawnTool");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par2World.isRemote)
        {
            // URL location = ItemSpawnTool.class.getProtectionDomain().getCodeSource().getLocation();
            //String string = location.getPath();

            //System.out.println(location.getFile());
            Schematic scem = new Schematic("fireflower").load();
        }
        return par1ItemStack;
    }
}
