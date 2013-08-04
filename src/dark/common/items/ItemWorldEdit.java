package dark.common.items;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.common.DarkBotMain;
import dark.common.gen.DarkSchematic;
import dark.common.hive.spire.HiveSpire;
import dark.common.hive.spire.TileEntitySpire;
import dark.common.prefab.Pair;
import dark.common.prefab.Pos;
import dark.common.prefab.PosWorld;

public class ItemWorldEdit extends Item
{
    static Icon wand;
    static Icon place;
    static Icon spire;
    static HashMap<String, Pair<Pos, Pos>> playerPointSelection = new HashMap<String, Pair<Pos, Pos>>();
    static HashMap<String, DarkSchematic> playerSchematic = new HashMap<String, DarkSchematic>();

    public ItemWorldEdit(int par)
    {
        super(DarkBotMain.config.getItem("SpawnTool", par).getInt());
        this.setUnlocalizedName("SpawnTool");
        this.setCreativeTab(CreativeTabs.tabTools);
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
            return spire;
        }
        if (par1 == 2)
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
        spire = par1IconRegister.registerIcon(DarkBotMain.PREFIX + "SpirePlacer");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        //TODO add security check for use of item
        //TODO add shift rotation system to change tool selection rather than meta data
        //TODO add rotation code
        //TODO add deletion code
        //TODO add clear code
        if (!world.isRemote && player != null)
        {
            String user = player.username;
            Pos pos = null;
            Pos pos2 = null;
            DarkSchematic schematic = null;
            if (playerPointSelection.containsKey(user))
            {
                pos = playerPointSelection.get(user).getOne();
                pos2 = playerPointSelection.get(user).getTwo();
            }
            if (playerSchematic.containsKey(user))
            {
                schematic = playerSchematic.get(user);
            }
            /* Load schematic from world */
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
                if (pos != null && pos2 != null)
                {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh.mm.ss");
                    String name = user + "_Schematic_" + dateFormat.format(new Date());
                    schematic = new DarkSchematic(name).loadWorldSelection(world, pos, pos2).save();

                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Saved Schematic  " + name));

                    //player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Copied region  "));
                    pos = null;
                    pos2 = null;
                    playerSchematic.put(user, schematic);
                }
                playerPointSelection.put(user, new Pair<Pos, Pos>(pos, pos2));
            }
            else if (stack.getItemDamage() == 1)
            {
                HiveSpire hiveSpire = HiveSpire.getSpire(new PosWorld(world, pos), 30);
                if (hiveSpire == null)
                {
                    TileEntitySpire spire = new TileEntitySpire();
                    spire.worldObj = world;
                    spire.xCoord = x;
                    spire.yCoord = y;
                    spire.zCoord = z;
                    hiveSpire = new HiveSpire(spire);
                }
                player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Building Max Size Spire at " + new Pos(x, y, z).toString()));
                HiveSpire.buildSpire(hiveSpire, HiveSpire.MAX_SIZE);
            }
            else if (stack.getItemDamage() == 2)
            {
                if (schematic != null)
                {
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Pasting schematic"));
                    schematic.build(new PosWorld(world, x, y, z), true);
                }
                else
                {
                    player.sendChatToPlayer(ChatMessageComponent.func_111066_d("No schematic loaded"));
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this.itemID, 1, 0));
        par3List.add(new ItemStack(this.itemID, 1, 1));
        par3List.add(new ItemStack(this.itemID, 1, 2));
    }
}
