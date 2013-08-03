package dark.common.items;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import dark.common.DarkBotMain;
import dark.common.gen.DarkSchematic;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class ItemSpawnTool extends Item
{
    static Icon wand;
    static Icon place;

    boolean flip = false;
    Pos pos;
    Pos pos2;
    Pos pos3;

    public ItemSpawnTool(int par)
    {
        super(DarkBotMain.config.getItem("SpawnTool", par).getInt());
        this.setUnlocalizedName("SpawnTool");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int par1)
    {
        if (par1 == 0)
        {
            return wand;
        }
        if (par1 == 1)
        {
            return place;
        }
        return this.itemIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        wand = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "WorldEditWand");
        place = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "WorldEditPlacer");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (stack.getItemDamage() == 0)
            {
                if (pos == null)
                {
                    pos = new Pos(x, y, z);
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Pos one set to " + pos.toString()));
                }
                else if (pos2 == null)
                {
                    pos2 = new Pos(x, y, z);
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Pos2 one set to " + pos2.toString()));
                }
                else if (pos3 == null)
                {
                    pos3 = new Pos(x, y, z);
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Center set to " + pos3.toString()));
                }
                if (pos3 != null && pos2 != null && pos != null)
                {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh.mm.ss");
                    new DarkSchematic("Schematic_" + dateFormat.format(new Date())).loadWorldSelection(world, pos, pos2, pos3).save();
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Saved Schematic"));
                    pos = null;
                    pos2 = null;
                }
            }
            else if (stack.getItemDamage() == 1)
            {
                System.out.println("Calling to build");
                new DarkSchematic("SpireRoom").load().build(new PosWorld(world, x, y, z), true, null);

            }

            return true;
            // URL location = ItemSpawnTool.class.getProtectionDomain().getCodeSource().getLocation();
            //String string = location.getPath();

            //System.out.println(location.getFile());
            //McEditSchematic scem = new McEditSchematic("TowerOne").load();
            //par3EntityPlayer.setPosition(par3EntityPlayer.posX, par3EntityPlayer.posY + scem.height, par3EntityPlayer.posZ);
            //scem.build(new PosWorld(par2World, par3EntityPlayer.posX, par3EntityPlayer.posY - scem.height, par3EntityPlayer.posZ), true);
        }
        return false;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this.itemID, 1, 0));
        par3List.add(new ItemStack(this.itemID, 1, 1));
    }
}
