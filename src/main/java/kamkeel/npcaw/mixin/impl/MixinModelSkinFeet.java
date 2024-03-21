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
import riskyken.armourersWorkshop.client.model.skin.ModelSkinFeet;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;

@Mixin(ModelSkinFeet.class)
public abstract class MixinModelSkinFeet extends AbstractModelSkin {

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

    @Inject(method = "renderRightFoot", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderRightLegTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            float x = (1 - legs.scaleX) * 0.125f;
            float y = npc.modelData.getBodyY() + (1 - legs.scaleY) * -0.1f;
            GL11.glTranslatef(x, y, 0);
        }
    }

    @Inject(method = "renderRightFoot", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 2, shift = At.Shift.AFTER), remap = false)
    public void renderRightLegScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            GL11.glScalef(legs.scaleX, legs.scaleY, legs.scaleZ);
        }
    }


    @Inject(method = "renderLeftFoot", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderLeftLegTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            float x = (1 - legs.scaleX) * 0.125f;
            float y = npc.modelData.getBodyY() + (1 - legs.scaleY) * -0.1f;
            GL11.glTranslatef(-x, y, 0);
        }
    }

    @Inject(method = "renderLeftFoot", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 2, shift = At.Shift.AFTER), remap = false)
    public void renderLeftLegScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            GL11.glScalef(legs.scaleX, legs.scaleY, legs.scaleZ);
        }
    }
}
