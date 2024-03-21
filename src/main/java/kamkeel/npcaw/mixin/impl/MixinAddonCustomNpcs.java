package kamkeel.npcaw.mixin.impl;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.entity.EntityNPCInterface;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import riskyken.armourersWorkshop.api.common.skin.IEntityEquipment;
import riskyken.armourersWorkshop.common.addons.AddonCustomNPCS;

@Mixin(AddonCustomNPCS.SkinnableEntityCustomNPCRenderer.class)
public abstract class MixinAddonCustomNpcs {

    @Inject(method = "render", at=@At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glScalef(FFF)V", ordinal = 0, shift = At.Shift.BEFORE), remap = false)
    public void renderNPCScale(EntityLivingBase entity, RendererLivingEntity renderer, double x, double y, double z, IEntityEquipment entityEquipment, CallbackInfo ci){
        if(entity instanceof EntityNPCInterface npc){
            GL11.glScalef(0.2f * npc.display.modelSize, 0.2f * npc.display.modelSize, 0.2f * npc.display.modelSize);
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 3, remap = false)
    public double modifyRot(double originalRot, EntityLivingBase entity, RendererLivingEntity renderer, double x, double y, double z, IEntityEquipment entityEquipment) {
        if(!(entity instanceof EntityNPCInterface npc))
            return originalRot;

        if (npc.isDrawn) {
            originalRot = Math.toRadians(entity.renderYawOffset);
        }
        return originalRot;
    }
}
