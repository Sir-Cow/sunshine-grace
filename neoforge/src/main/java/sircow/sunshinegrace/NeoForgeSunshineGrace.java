package sircow.sunshinegrace;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import sircow.sunshinegrace.config.NeoForgeConfig;

@Mod(Constants.MOD_ID)
public class NeoForgeSunshineGrace {
    public NeoForgeSunshineGrace(IEventBus eventBus) {
        CommonClass.init();
        NeoForgeConfig.loadServer();
    }
}
