package sircow.sunshinegrace;

import net.fabricmc.api.ModInitializer;
import sircow.sunshinegrace.effect.FabricModEffects;
import sircow.sunshinegrace.event.FabricModEvents;

public class SunshineGrace implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricModEffects.registerFabricModEffects();
        FabricModEvents.registerModEvents();
    }
}
