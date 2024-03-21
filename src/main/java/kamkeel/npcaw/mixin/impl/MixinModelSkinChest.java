package kamkeel.npcaw.mixin.impl;

import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.data.ModelScalePart;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinDye;
import riskyken.armourersWorkshop.client.model.skin.AbstractModelSkin;
import riskyken.armourersWorkshop.client.model.skin.ModelSkinChest;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;

@Mixin(ModelSkinChest.class)
public abstract class MixinModelSkinChest extends AbstractModelSkin {

    @Unique
    private Entity customNPC_AWAddon$renderingEntity = null;

    @Inject(method = "render", at = @At("HEAD"), remap = false)
    public void renderStart(Entity entity, Skin armourData, boolean showSkinPaint, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        this.customNPC_AWAddon$renderingEntity = entity;
    }

    @Inject(method = "render", at = @At("TAIL"), remap = false)
    public void renderEnd(Entity entity, Skin armourData, boolean showSkinPaint, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        this.customNPC_AWAddon$renderingEntity = null;
    }

    @Inject(method = "renderChest", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderBeforeChest(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart body = npc.modelData.modelScale.body;
            float y = npc.modelData.getBodyY();
            GL11.glTranslatef(0, y, 0);

            GL11.glRotatef((float)Math.toDegrees((double)this.bipedBody.rotateAngleZ), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float)Math.toDegrees((double)this.bipedBody.rotateAngleY), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)Math.toDegrees((double)this.bipedBody.rotateAngleX), 1.0F, 0.0F, 0.0F);

            GL11.glTranslatef(0, 0, 0);
            GL11.glScalef(body.scaleX, body.scaleY, body.scaleZ);
        }
    }

    @Inject(method = "renderRightArm", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    private void renderRightArmTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, boolean override, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart arms = npc.modelData.modelScale.arms;
            float x = (1 - npc.modelData.modelScale.body.scaleX) * 0.25f + (1 - arms.scaleX) * 0.075f;
            float y = npc.modelData.getBodyY() + (1 - arms.scaleY) * -0.1f;
            GL11.glTranslatef(x, y, 0.0F);
        }
    }

    @Inject(method = "renderRightArm", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 2, shift = At.Shift.AFTER), remap = false)
    private void renderRightArmScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, boolean override, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart arms = npc.modelData.modelScale.arms;
            GL11.glScalef(arms.scaleX, arms.scaleY, arms.scaleZ);
        }
    }


    @Inject(method = "renderLeftArm", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 1, shift = At.Shift.AFTER), remap = false)
    public void renderBeforeLeftArmTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, boolean override, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart arms = npc.modelData.modelScale.arms;
            float x = (1 - npc.modelData.modelScale.body.scaleX) * 0.25f + (1 - arms.scaleX) * 0.075f;
            float y = npc.modelData.getBodyY() + (1 - arms.scaleY) * -0.1f;
            GL11.glTranslatef(-x, y, 0);
        }
    }

    @Inject(method = "renderLeftArm", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 2, shift = At.Shift.AFTER), remap = false)
    public void renderBeforeLeftArmScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, boolean override, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart arms = npc.modelData.modelScale.arms;
            GL11.glScalef(arms.scaleX, arms.scaleY, arms.scaleZ);
        }
    }
}
