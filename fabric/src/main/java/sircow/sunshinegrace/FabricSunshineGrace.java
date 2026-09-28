package sircow.sunshinegrace;

import net.fabricmc.api.ModInitializer;
import sircow.sunshinegrace.config.FabricConfig;
import sircow.sunshinegrace.effect.FabricModEffects;
import sircow.sunshinegrace.event.FabricModEvents;

public class FabricSunshineGrace implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricConfig.loadServer();
        FabricModEffects.registerFabricModEffects();
        FabricModEvents.registerFabricModEvents();
    }
}
