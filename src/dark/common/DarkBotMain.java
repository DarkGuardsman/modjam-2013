package dark.common;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import dark.common.items.ItemBlockMain;
import dark.common.items.ItemSpawnTool;
import dark.common.tiles.BlockDecor;

@Mod(modid = DarkBotMain.MOD_ID, name = DarkBotMain.MOD_NAME, version = DarkBotMain.VERSION)
@NetworkMod(channels = { DarkBotMain.MAIN_CHANNEL, DarkBotMain.AI_CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
public class DarkBotMain
{
    public static final String MOD_ID = "DarkBots";
    public static final String MOD_NAME = "Dark Bots";

    public static final String VERSION = "MODJAM" + "@BUILD";

    public static final String MAIN_CHANNEL = "DARKBOTMAIN";
    public static final String AI_CHANNEL = "DARKAIMAIN";

    public static final String DOMAIN = "dark";
    public static final String PREFIX = DOMAIN + ":";

    public static Item spawnTool;
    public static Block blockDeco;
    public static Block blockCore;

    public static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), "Dark/BotMain.cfg"));

    @Instance(MOD_ID)
    public static DarkBotMain instance;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        config.load();
        spawnTool = new ItemSpawnTool(12000);
        blockDeco = new BlockDecor(2000);
        config.save();
        //TODO reg blocks
        //TODO reg oreNames

        GameRegistry.registerBlock(blockDeco, ItemBlockMain.class, "SpireDecoBlock");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //TODO reg handlers
        //TODO reg tiles
        //TODO reg entities
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        //TODO reg recipes
    }
}
