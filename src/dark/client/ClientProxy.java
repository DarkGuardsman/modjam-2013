package dark.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import dark.client.renders.RenderCore;
import dark.client.renders.RenderDefenderTwo;
import dark.common.CommonProxy;
import dark.common.hive.entity.EntityDefender;
import dark.common.hive.spire.TileEntitySpire;

public class ClientProxy extends CommonProxy
{

    public void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpire.class, new RenderCore());

        RenderingRegistry.registerEntityRenderingHandler(EntityDefender.class, new RenderDefenderTwo());
    }
}
