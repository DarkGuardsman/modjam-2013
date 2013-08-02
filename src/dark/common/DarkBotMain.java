package dark.common;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

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
    public static final String PREFIX = DOMAIN+":";

    public static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(),"Dark/BotMain.cfg"));

    @Instance(MOD_ID)
    public static DarkBotMain instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        config.load();


        config.save();
        //TODO load configs
        //TODO reg blocks
        //TODO reg oreNames
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
