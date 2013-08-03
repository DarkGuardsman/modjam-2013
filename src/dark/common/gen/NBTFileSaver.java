package dark.common.gen;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class NBTFileSaver
{

    public File getSaveDirectory()
    {
        File saveDirectory = getSaveDirectory(MinecraftServer.getServer().getFolderName());
        File file = new File(saveDirectory, "dark");

        if (!saveDirectory.exists())
        {
            saveDirectory.mkdir();
        }

        if (!file.exists())
        {
            file.mkdir();
        }

        return file;
    }

    public static File getSaveDirectory(String worldName)
    {
        File parent = getBaseDirectory();

        if (FMLCommonHandler.instance().getSide().isClient())
        {
            parent = new File(getBaseDirectory(), "saves" + File.separator);
        }

        return new File(parent, worldName + File.separator);
    }

    public static File getBaseDirectory()
    {
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            FMLClientHandler.instance().getClient();
            return Minecraft.getMinecraft().mcDataDir;
        }
        else
        {
            return new File(".");
        }
    }

}
