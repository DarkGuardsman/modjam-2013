package dark.client.renders;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import dark.common.DarkBotMain;

/** Basic Advanced Obj modeler render for quick test runs and scaling
 *
 * @author DarkGuardsman */
public class RenderCore extends TileEntitySpecialRenderer
{

    private IModelCustom modelCore;
    private IModelCustom modelClaw;
    private float scale;
    private String texture;
    public static float xChange, yChange, zChange, r;

    public RenderCore()
    {
        modelCore = AdvancedModelLoader.loadModel("/assets/dark/models/Core.Render.obj");
        modelClaw = AdvancedModelLoader.loadModel("/assets/dark/models/Core.Holder.obj");
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double xx, double yy, double zz, float f)
    {
        render(entity, xx, yy, zz);
    }

    private void render(TileEntity laser, double xx, double yy, double zz)
    {

        GL11.glPushMatrix();
        GL11.glTranslated(xx + xChange + .50, yy + yChange + 2.2, zz + zChange + .50);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(1, 1, 1);
        GL11.glRotatef(r, 0F, 1F, 0F);

        r += 2;
        if(r > 180)
        {
            r = r % 180;
        }
        if(r < -180)
        {
            r = -(Math.abs(r) % 180);
        }

        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Core.Render.png"));
        modelCore.renderAll();

        GL11.glTranslated(0, -.5, 0);
        GL11.glScalef(.8f, .8f, .8f);
        GL11.glRotatef(Math.-r, 0F, 1F, 0F);
        FMLClientHandler.instance().getClient().renderEngine.func_110577_a(new ResourceLocation(DarkBotMain.DOMAIN, "textures/uv/Core.Holder.png"));
        modelClaw.renderAll();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();

    }
}
