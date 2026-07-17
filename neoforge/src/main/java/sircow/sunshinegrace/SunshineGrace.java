package sircow.sunshinegrace;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SunshineGrace {
    public SunshineGrace(IEventBus eventBus) {
        CommonClass.init();
    }
}
