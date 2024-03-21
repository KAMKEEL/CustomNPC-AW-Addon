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
import riskyken.armourersWorkshop.client.model.skin.ModelSkinLegs;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;

@Mixin(ModelSkinLegs.class)
public abstract class MixinModelSkinLegs extends AbstractModelSkin {

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

    @Inject(method = "renderSkirt", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderSkirtTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            float y = npc.modelData.getLegsY();
            GL11.glTranslatef(0, y, 0);
            GL11.glScalef(legs.scaleX, legs.scaleY, legs.scaleZ);
        }
    }

    @Inject(method = "renderSkirt", at = @At(value = "INVOKE", target = "Lriskyken/armourersWorkshop/client/model/skin/ModelSkinLegs;renderPart(Lriskyken/armourersWorkshop/common/skin/data/SkinPart;FLriskyken/armourersWorkshop/api/common/skin/data/ISkinDye;[BDZ)V", shift = At.Shift.BEFORE), remap = false)
    public void renderSkirtScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            GL11.glScalef(legs.scaleX, legs.scaleY, legs.scaleZ);
        }
    }

    @Inject(method = "renderRightLeg", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderRightLegTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            float x = (1 - legs.scaleX) * 0.125f;
            float y = npc.modelData.getBodyY() + (1 - legs.scaleY) * -0.1f;
            GL11.glTranslatef(x, y, 0);
        }
    }

    @Inject(method = "renderRightLeg", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 2, shift = At.Shift.AFTER), remap = false)
    public void renderRightLegScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            GL11.glScalef(legs.scaleX, legs.scaleY, legs.scaleZ);
        }
    }


    @Inject(method = "renderLeftLeg", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderLeftLegTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            float x = (1 - legs.scaleX) * 0.125f;
            float y = npc.modelData.getBodyY() + (1 - legs.scaleY) * -0.1f;
            GL11.glTranslatef(-x, y, 0);
        }
    }

    @Inject(method = "renderLeftLeg", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 2, shift = At.Shift.AFTER), remap = false)
    public void renderLeftLegScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart legs = npc.modelData.modelScale.legs;
            GL11.glScalef(legs.scaleX, legs.scaleY, legs.scaleZ);
        }
    }
}
