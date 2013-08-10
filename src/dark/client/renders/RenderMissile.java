package dark.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import dark.client.models.ModelMissile;
import dark.common.DarkBotMain;
import dark.common.entity.EntityBombMissile;

public class RenderMissile extends Render
{
    ModelMissile missil = new ModelMissile();

    @Override
    public void doRender(Entity entity, double xx, double yy, double zz, float f, float f1)
    {
        if (entity instanceof EntityBombMissile)
        {
            this.renderMissile((EntityBombMissile) entity, xx, yy, zz, f, f1);
        }
    }

    public void renderMissile(EntityBombMissile entity, double xx, double yy, double zz, float f, float f1)
    {
        this.func_110777_b(entity);
        GL11.glPushMatrix();
        GL11.glTranslated(xx, yy, zz);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(1, 1, 1);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1 + 90.0F, 0.0F, 0.0F, 1.0F);

        missil.render();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();

    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        return new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Missile.png");
    }

}
