package dark.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderObjModel extends Render
{

    private IModelCustom modelTurret;
    private float scale;

    public RenderObjModel(float scaleDown)
    {
        modelTurret = AdvancedModelLoader.loadModel("");
        scale = scaleDown;
    }

    @Override
    public void doRender(Entity entity, double xx, double yy, double zz, float f, float f1)
    {
        render(entity, xx, yy, zz, scale);
    }

    private void render(Entity laser, double xx, double yy, double zz, float s)
    {

        GL11.glPushMatrix();

        GL11.glTranslated(xx, yy, zz);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(s, s, s);

        GL11.glRotatef(0, 0F, 1F, 0F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture();
        modelTurret.renderAll();

        GL11.glTranslated(0, 2.0, 0);
        GL11.glRotatef(0, 0f, 0F, 1f);
        GL11.glTranslated(0, -(2.0), 0);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();

    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
