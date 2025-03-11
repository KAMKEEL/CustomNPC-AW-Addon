package kamkeel.npcaw.mixin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import kamkeel.npcaw.config.ConfigAWMixin;
import noppes.npcs.config.ConfigMixin;

import java.io.File;
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

        String configPath = "config"  + File.separator + "CustomNpcPlus" + File.separator + "aw" + File.separator;
        ConfigAWMixin.init(new File(configPath + "mixin.cfg"));

        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
            // Fix Animation and Base Rotations
            mixins.add("client.MixinAbstractModelSkin");

            // Fix Inventory Render and Sizing
            mixins.add("client.MixinAddonCustomNpcs");

            // Rotation and Scaling
            mixins.add("client.MixinModelSkinHead");
            mixins.add("client.MixinModelSkinChest");
            mixins.add("client.MixinModelSkinLegs");
            mixins.add("client.MixinModelSkinFeet");
            mixins.add("client.MixinModelSkinWings");
        }

        if(ConfigAWMixin.useCacheFixMixin)
            mixins.add("MixinSkinCacheLocalFile");


        return mixins;
    }

}
