package dark.client;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import dark.client.renders.RenderBossGigus;
import dark.client.renders.RenderCore;
import dark.client.renders.RenderDefenderTwo;
import dark.client.renders.RenderMissile;
import dark.common.CommonProxy;
import dark.common.entity.EntityBossGigus;
import dark.common.entity.EntityDefender;
import dark.common.entity.EntityBombMissile;
import dark.common.hive.spire.TileEntitySpireCore;

public class ClientProxy extends CommonProxy
{

    public void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpireCore.class, new RenderCore());

        RenderingRegistry.registerEntityRenderingHandler(EntityDefender.class, new RenderDefenderTwo());
        RenderingRegistry.registerEntityRenderingHandler(EntityBombMissile.class, new RenderMissile());
        RenderingRegistry.registerEntityRenderingHandler(EntityBossGigus.class, new RenderBossGigus());

        MinecraftForge.EVENT_BUS.register(SoundHandler.INSTANCE);
    }
}
