package kamkeel.npcaw.mixin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class NPCAWLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.npcaw.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
            // Fix Animation and Base Rotations
            mixins.add("MixinAbstractModelSkin");

            // Fix Inventory Render and Sizing
            mixins.add("MixinAddonCustomNpcs");

            // Rotation and Scaling
            mixins.add("MixinModelSkinHead");
            mixins.add("MixinModelSkinChest");
            mixins.add("MixinModelSkinLegs");
            mixins.add("MixinModelSkinFeet");
            mixins.add("MixinModelSkinWings");
        }
        return mixins;
    }

}
