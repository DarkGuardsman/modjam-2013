// Date: 8/5/2013 10:33:05 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package dark.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMissile extends ModelBase
{
    //fields
    ModelRenderer Body;
    ModelRenderer Fin;
    ModelRenderer Head;

    public ModelMissile()
    {
        textureWidth = 64;
        textureHeight = 32;

        Body = new ModelRenderer(this, 12, 0);
        Body.addBox(-1F, 0F, -1F, 2, 12, 2);
        Body.setRotationPoint(0F, 11F, 0F);
        Body.setTextureSize(64, 32);
        Body.mirror = true;
        setRotation(Body, 0F, 0F, 0F);
        Fin = new ModelRenderer(this, 0, 0);
        Fin.addBox(1F, 20F, -0.5F, 1, 4, 1);
        Fin.setRotationPoint(0F, 0F, 0F);
        Fin.setTextureSize(64, 32);
        Fin.mirror = true;
        setRotation(Fin, 0F, 0F, 0F);
        Head = new ModelRenderer(this, 22, 0);
        Head.addBox(-1.5F, -3F, -1.5F, 3, 3, 3);
        Head.setRotationPoint(0F, 11F, 0F);
        Head.setTextureSize(64, 32);
        Head.mirror = true;
        setRotation(Head, 0F, 0F, 0F);
    }

    public void render()
    {
        //Body.render(0.0625f);
        //Fin.render(0.0625f);
        Head.render(0.0625f);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
