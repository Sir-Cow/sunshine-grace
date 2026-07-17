package sircow.sunshinegrace;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import sircow.sunshinegrace.effect.NeoForgeModEffects;

@Mod(Constants.MOD_ID)
public class SunshineGrace {
    public SunshineGrace(IEventBus eventBus) {
        CommonClass.init();
        NeoForgeModEffects.init(eventBus);
    }
}
