package dark.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import dark.client.models.ModelMissile;

public class RenderMissile extends Render
{
    ModelMissile missil = new ModelMissile();

    @Override
    public void doRender(Entity entity, double xx, double yy, double zz, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(xx, yy, zz);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(1, 1, 1);
        GL11.glRotatef(-entity.rotationYaw, 0F, 1F, 0F);
        GL11.glRotatef(-entity.rotationPitch, 1F, 0F, 0F);
        missil.render();

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
