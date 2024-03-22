package kamkeel.npcaw.mixin.impl;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.npcs.api.handler.data.IFrame;
import noppes.npcs.api.handler.data.IFramePart;
import noppes.npcs.client.ClientEventHandler;
import noppes.npcs.controllers.data.FramePart;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.data.ModelScalePart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinDye;
import riskyken.armourersWorkshop.client.model.skin.AbstractModelSkin;
import riskyken.armourersWorkshop.common.skin.data.Skin;

@Mixin(AbstractModelSkin.class)
public abstract class MixinAbstractModelSkin extends ModelBiped {

    @Inject(method = "render(Lnet/minecraft/entity/Entity;FFFFFF)V", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER))
    public void render(Entity entity, float limb1, float limb2, float limb3, float headY, float headX, float scale, CallbackInfo ci){
        if(ClientEventHandler.renderingNpc != null){
            if(ClientEventHandler.renderingNpc.display.animationData.isActive()){
                IFrame frame = ClientEventHandler.renderingNpc.display.animationData.animation.currentFrame();
                if(frame != null)
                    customNPC_AWAddon$setAnimationRotation(frame);
            } else {
                if(entity instanceof EntityCustomNpc npc){
                    if(npc.modelData.enableRotation && !npc.display.animationData.isActive()){
                        if(customNPC_AWAddon$isRotationActive(npc)){
                            float pi = (float) Math.PI;

                            if(!npc.modelData.rotation.head.disabled){
                                bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX = npc.modelData.rotation.head.rotationX * pi;
                                bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY = npc.modelData.rotation.head.rotationY * pi;
                                bipedHeadwear.rotateAngleZ = bipedHead.rotateAngleZ = npc.modelData.rotation.head.rotationZ * pi;
                            }

                            if(!npc.modelData.rotation.body.disabled){
                                bipedBody.rotateAngleX = npc.modelData.rotation.body.rotationX * pi;
                                bipedBody.rotateAngleY = npc.modelData.rotation.body.rotationY * pi;
                                bipedBody.rotateAngleZ = npc.modelData.rotation.body.rotationZ * pi;
                            }

                            if(!npc.modelData.rotation.larm.disabled){
                                bipedLeftArm.rotateAngleX = npc.modelData.rotation.larm.rotationX * pi;
                                bipedLeftArm.rotateAngleY = npc.modelData.rotation.larm.rotationY * pi;
                                bipedLeftArm.rotateAngleZ = npc.modelData.rotation.larm.rotationZ * pi;

                                if(!npc.display.disableLivingAnimation){
                                    this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(limb2 * 0.09F) * 0.05F + 0.05F;
                                    this.bipedLeftArm.rotateAngleX -= MathHelper.sin(limb2 * 0.067F) * 0.05F;
                                }
                            }

                            if(!npc.modelData.rotation.rarm.disabled){
                                bipedRightArm.rotateAngleX = npc.modelData.rotation.rarm.rotationX * pi;
                                bipedRightArm.rotateAngleY = npc.modelData.rotation.rarm.rotationY * pi;
                                bipedRightArm.rotateAngleZ = npc.modelData.rotation.rarm.rotationZ * pi;

                                if(!npc.display.disableLivingAnimation){
                                    this.bipedRightArm.rotateAngleZ += MathHelper.cos(limb2 * 0.09F) * 0.05F + 0.05F;
                                    this.bipedRightArm.rotateAngleX += MathHelper.sin(limb2 * 0.067F) * 0.05F;
                                }
                            }

                            if(!npc.modelData.rotation.rleg.disabled){
                                bipedRightLeg.rotateAngleX = npc.modelData.rotation.rleg.rotationX * pi;
                                bipedRightLeg.rotateAngleY = npc.modelData.rotation.rleg.rotationY * pi;
                                bipedRightLeg.rotateAngleZ = npc.modelData.rotation.rleg.rotationZ * pi;
                            }

                            if(!npc.modelData.rotation.lleg.disabled){
                                bipedLeftLeg.rotateAngleX = npc.modelData.rotation.lleg.rotationX * pi;
                                bipedLeftLeg.rotateAngleY = npc.modelData.rotation.lleg.rotationY * pi;
                                bipedLeftLeg.rotateAngleZ = npc.modelData.rotation.lleg.rotationZ * pi;
                            }
                        }
                    }
                }
            }
        }
    }

    @Unique
    public boolean customNPC_AWAddon$isRotationActive(EntityCustomNpc npc) {
        if(!npc.isEntityAlive())
            return false;

        if(npc.modelData.rotation.whileAttacking && npc.isAttacking() || npc.modelData.rotation.whileMoving && npc.isWalking() || npc.modelData.rotation.whileStanding && !npc.isWalking())
            return true;
        return false;
    }

    @Unique
    private void customNPC_AWAddon$setAnimationRotation(IFrame frame){
        IFramePart[] parts = frame.getParts();
        for(IFramePart part : parts){
            customNPC_AWAddon$setPartRotation(customNPC_AWAddon$getRendererByPartId(part.getPartId()), (FramePart) part);
        }
    }

    @Unique
    private ModelRenderer customNPC_AWAddon$getRendererByPartId(int id){
        switch(id){
            case 0:
                return this.bipedHead;
            case 1:
                return this.bipedBody;
            case 2:
                return this.bipedRightArm;
            case 3:
                return this.bipedLeftArm;
            case 4:
                return this.bipedRightLeg;
            case 5:
                return this.bipedLeftLeg;
        }
        return null;
    }

    @Unique
    private void customNPC_AWAddon$setPartRotation(ModelRenderer part, FramePart framePart){
        if(part == null || framePart == null)
            return;

        framePart.interpolateOffset();
        framePart.interpolateAngles();

        part.rotationPointX += framePart.prevPivots[0];
        part.rotationPointY += framePart.prevPivots[1];
        part.rotationPointZ += framePart.prevPivots[2];
        part.rotateAngleX = framePart.prevRotations[0];
        part.rotateAngleY = framePart.prevRotations[1];
        part.rotateAngleZ = framePart.prevRotations[2];
    }

}
