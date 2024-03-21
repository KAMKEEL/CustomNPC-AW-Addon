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
import riskyken.armourersWorkshop.client.model.skin.ModelSkinWings;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.common.skin.type.wings.SkinWings;

@Mixin(ModelSkinWings.class)
public abstract class MixinModelSkinWings extends AbstractModelSkin {

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

    @Inject(method = "renderRightWing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    private void renderRightWingTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, double distance, double angle, boolean doLodLoading, SkinWings.MovementType movmentType, CallbackInfo ci) {
        if (customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc) {
            float y = npc.modelData.getBodyY();
            GL11.glTranslatef(0, y, 0);
        }
    }

    @Inject(method = "renderRightWing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 0, shift = At.Shift.BEFORE), remap = false)
    private void renderRightWingRotate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, double distance, double angle, boolean doLodLoading, SkinWings.MovementType movmentType, CallbackInfo ci) {
        if (customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc) {
            GL11.glRotatef((float)Math.toDegrees((double)this.bipedBody.rotateAngleX), 1.0F, 0.0F, 0.0F);
        }
    }

    @Inject(method = "renderRightWing", at = @At(value = "INVOKE", target = "Lriskyken/armourersWorkshop/client/model/skin/ModelSkinWings;renderPart(Lriskyken/armourersWorkshop/common/skin/data/SkinPart;FLriskyken/armourersWorkshop/api/common/skin/data/ISkinDye;[BDZ)V", shift = At.Shift.BEFORE), remap = false)
    private void renderRightWingScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, double distance, double angle, boolean doLodLoading, SkinWings.MovementType movmentType, CallbackInfo ci) {
        if (customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc) {
            ModelScalePart body = npc.modelData.modelScale.body;
            GL11.glScalef(body.scaleX, body.scaleY, body.scaleZ);
        }
    }


    @Inject(method = "renderLeftWing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    private void renderLeftWingTranslate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, double distance, double angle, boolean doLodLoading, SkinWings.MovementType movmentType, CallbackInfo ci) {
        if (customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc) {
            float y = npc.modelData.getBodyY();
            GL11.glTranslatef(0, y, 0);
        }
    }

    @Inject(method = "renderLeftWing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 0, shift = At.Shift.BEFORE), remap = false)
    private void renderRightLeftRotate(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, double distance, double angle, boolean doLodLoading, SkinWings.MovementType movmentType, CallbackInfo ci) {
        if (customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc) {
            GL11.glRotatef((float)Math.toDegrees((double)this.bipedBody.rotateAngleX), 1.0F, 0.0F, 0.0F);
        }
    }

    @Inject(method = "renderLeftWing", at = @At(value = "INVOKE", target = "Lriskyken/armourersWorkshop/client/model/skin/ModelSkinWings;renderPart(Lriskyken/armourersWorkshop/common/skin/data/SkinPart;FLriskyken/armourersWorkshop/api/common/skin/data/ISkinDye;[BDZ)V", shift = At.Shift.BEFORE), remap = false)
    private void renderLeftWingScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColour, double distance, double angle, boolean doLodLoading, SkinWings.MovementType movmentType, CallbackInfo ci) {
        if (customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc) {
            ModelScalePart body = npc.modelData.modelScale.body;
            GL11.glScalef(body.scaleX, body.scaleY, body.scaleZ);
        }
    }
}
