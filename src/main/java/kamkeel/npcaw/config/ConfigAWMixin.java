package kamkeel.npcaw.config;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigAWMixin {
    public static Configuration config;

    public final static String GENERAL = "General";

    public static boolean useCacheFixMixin = true;

    public static void init(File configFile) {
        config = new Configuration(configFile);

        try {
            config.load();

            // General
            useCacheFixMixin = config.get(GENERAL, "Cache Fix Mixin", true, "Fixes failing skin cache grabbing mixin").getBoolean(true);

        } catch (Exception e) {
            FMLLog.log(Level.ERROR, e, "AW Addon has had a problem loading its mixin configuration");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
