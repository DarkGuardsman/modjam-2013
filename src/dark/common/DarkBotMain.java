package dark.common;

import java.io.File;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dark.common.entity.EntityBombMissile;
import dark.common.entity.EntityBossGigus;
import dark.common.entity.EntityDefender;
import dark.common.entity.ItemBotSpawner;
import dark.common.gen.WorldGen;
import dark.common.hive.Hivemind;
import dark.common.hive.spire.BlockSpireCore;
import dark.common.hive.spire.TileEntitySpireCore;
import dark.common.items.DispenserBehaviorMissile;
import dark.common.items.ItemBlockMain;
import dark.common.items.ItemMain;
import dark.common.items.ItemRocketPod;
import dark.common.items.ItemWorldEdit;
import dark.common.tiles.BlockCreep;
import dark.common.tiles.BlockDecor;

@Mod(modid = DarkBotMain.MOD_ID, name = DarkBotMain.MOD_NAME, version = DarkBotMain.VERSION)
@NetworkMod(channels = { DarkBotMain.MAIN_CHANNEL, DarkBotMain.AI_CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
public class DarkBotMain
{
    public static final String MOD_ID = "DarkBots";
    public static final String MOD_NAME = "Dark Bots";

    @Metadata(DarkBotMain.MOD_ID)
    public static ModMetadata meta;

    public static final String VERSION = "MODJAM" + "@BUILD";

    public static final String MAIN_CHANNEL = "DARKBOTMAIN";
    public static final String AI_CHANNEL = "DARKAIMAIN";

    public static final String DOMAIN = "dark";
    public static final String PREFIX = DOMAIN + ":";

    @Instance(DarkBotMain.MOD_ID)
    public static DarkBotMain instance;

    @SidedProxy(clientSide = "dark.client.ClientProxy", serverSide = "dark.common.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), "Dark/BotMain.cfg"));

    public static Item worldEditTool, droneSpawnTool, itemMissile, itemRocketPod;
    public static Block blockDeco, blockCreep, blockCore;

    /** Disables the hiveminds and there activity */
    public static boolean disableHive = false;
    /** Prevents the hives from launching attacks */
    public static boolean disableHiveAgression = false;
    /** Disables hive drones and spawns */
    public static boolean disableDrones = false;
    /** Disable hive world gen buildings */
    public static boolean disableWorldGen = false;
    /** Disables spires from rebuilding */
    public static boolean disableSpireBuilding = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        this.loadModMeta();
        config.load();
        worldEditTool = new ItemWorldEdit(12000);
        droneSpawnTool = new ItemBotSpawner(12001);
        itemMissile = new ItemMain(12002, "Missile", CreativeTabs.tabCombat);
        itemRocketPod = new ItemRocketPod(12003);
        blockDeco = new BlockDecor(2000);
        blockCore = new BlockSpireCore(2001);
        blockCreep = new BlockCreep(2002);
        config.save();
        //TODO reg blocks
        //TODO reg oreNames

        GameRegistry.registerBlock(blockDeco, ItemBlockMain.class, "SpireDecoBlock");
        GameRegistry.registerBlock(blockCore, ItemBlockMain.class, "SpireCoreBlock");
        GameRegistry.registerBlock(blockCreep, ItemBlockMain.class, "SpireCreepBlock");
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(Hivemind.class);
        GameRegistry.registerTileEntity(TileEntitySpireCore.class, "HiveSpire");
        GameRegistry.registerWorldGenerator(new WorldGen());
        BlockDispenser.dispenseBehaviorRegistry.putObject(itemMissile, new DispenserBehaviorMissile());
        EntityRegistry.registerGlobalEntityID(EntityDefender.class, "GSMDefenderII", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityDefender.class, "GSMDefenderII", EntityRegistry.findGlobalUniqueEntityId(), instance, 60, 1, true);

        EntityRegistry.registerGlobalEntityID(EntityBombMissile.class, "GSMMissileOne", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityBombMissile.class, "GSMMissileOne", EntityRegistry.findGlobalUniqueEntityId(), instance, 60, 1, true);

        EntityRegistry.registerGlobalEntityID(EntityBossGigus.class, "GSMBossGigus", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityBossGigus.class, "GSMBossGigus", EntityRegistry.findGlobalUniqueEntityId(), instance, 60, 1, true);

        //TODO reg entities
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        //TODO reg recipes
        proxy.postInit();
    }

    public void loadModMeta()
    {
        /* MCMOD.INFO FILE BUILDER? */
        meta.modId = DarkBotMain.MOD_ID;
        meta.name = DarkBotMain.MOD_NAME;
        meta.description = "This mod is only a sample of what is to come in a series of hivemind drones designed to consume the world and challange the player";

        meta.url = "http://www.builtBroken.com";

        meta.logoFile = "textures/FM_Banner.png";
        meta.version = DarkBotMain.VERSION;
        meta.authorList = Arrays.asList(new String[] { "DarkGuardsman AKA DarkCow", "Hangcow", "Doppelgangerous" });
        meta.credits = "Please see the website.";
        meta.autogenerated = false;

    }
}
