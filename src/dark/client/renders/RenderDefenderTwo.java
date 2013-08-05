package dark.client.renders;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import dark.common.DarkBotMain;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderDefenderTwo extends Render
{
    private IModelCustom model;

    public RenderDefenderTwo()
    {
        model = AdvancedModelLoader.loadModel("/assets/dark/models/Drone.Wheel.obj");
    }
    @Override
    public void doRender(Entity entity, double xx, double yy, double zz, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(xx, yy, zz);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        GL11.glRotatef(entity.rotationYaw, 0F, 1F, 0F);

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(this.func_110775_a(entity));
        model.renderAll();


        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();


    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        return new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Drone.Wheel.png");
    }

}
