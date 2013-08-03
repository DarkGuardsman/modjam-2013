package dark.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import dark.client.renders.RenderCore;
import dark.common.CommonProxy;
import dark.common.hive.spire.TileEntitySpire;

public class ClientProxy extends CommonProxy
{

    public void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpire.class, new RenderCore("Core.Render", "Core.Render", 1));
    }
}
