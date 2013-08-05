package dark.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import dark.common.DarkBotMain;

public class RenderDefenderTwo extends Render
{
    private IModelCustom modelBody;
    private IModelCustom modelTrack;
    private IModelCustom modelWheel;
    private ResourceLocation body = new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Drone.Wheel.Body.png");
    private ResourceLocation track = new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Drone.Wheel.Track.png");
    private ResourceLocation wheel = new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Drone.Wheel.Wheel.png");

    public RenderDefenderTwo()
    {
        modelBody = AdvancedModelLoader.loadModel("/assets/dark/models/Drone.Wheel/Drone.Wheel.Body.obj");
        modelTrack = AdvancedModelLoader.loadModel("/assets/dark/models/Drone.Wheel/Drone.Wheel.Track.obj");
        modelWheel = AdvancedModelLoader.loadModel("/assets/dark/models/Drone.Wheel/Drone.Wheel.Wheel.obj");
    }

    @Override
    public void doRender(Entity entity, double xx, double yy, double zz, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(xx, yy+0.5, zz);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        float rotation = entity.rotationYaw - 90;
        GL11.glRotatef(rotation, 0F, 1F, 0F);

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(wheel);
        modelWheel.renderAll();

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(body);
        modelBody.renderAll();

        if (entity instanceof EntityLiving)
        {
            GL11.glRotatef(-rotation + ((EntityLiving)entity).rotationYawHead, 0F, 1F, 0F);
        }

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(track);
        modelTrack.renderAll();



        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();

    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        return new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Drone.Wheel.png");
    }

}
