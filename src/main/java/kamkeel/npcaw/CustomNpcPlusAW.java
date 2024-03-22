package kamkeel.npcaw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "npcaw",
    name = "CustomNPC+ AW Addon",
    version = "1.1",
    dependencies = "after:customnpcs;after:armourersWorkshop;",
    acceptableRemoteVersions = "*")
public class CustomNpcPlusAW {

    @SidedProxy(clientSide = "kamkeel.npcaw.client.ClientProxy", serverSide = "kamkeel.npcaw.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static CustomNpcPlusAW instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        proxy.preInit(ev);
    }
}
