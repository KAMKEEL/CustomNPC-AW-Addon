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
import riskyken.armourersWorkshop.client.model.skin.ModelSkinHead;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;

@Mixin(ModelSkinHead.class)
public abstract class MixinModelSkinHead extends AbstractModelSkin {

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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotated(DDDD)V", ordinal = 0, shift = At.Shift.BEFORE), remap = false)
    public void renderHeadTranslate(Entity entity, Skin skin, boolean showSkinPaint, ISkinDye skinDye, byte[] extraColour, boolean itemRender, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            float y = npc.modelData.getBodyY();
            GL11.glTranslatef(0, y, 0);
        }
    }

    @Inject(method = "renderHead", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    public void renderHeadScale(SkinPart part, float scale, ISkinDye skinDye, byte[] extraColours, double distance, boolean doLodLoading, CallbackInfo ci) {
        if(customNPC_AWAddon$renderingEntity instanceof EntityCustomNpc npc){
            ModelScalePart head = npc.modelData.modelScale.head;
            GL11.glScalef(head.scaleX, head.scaleY, head.scaleZ);
        }
    }
}
