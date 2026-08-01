package sircow.sunshinegrace.effect;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class FabricModEffects {
    public static void registerFabricModEffects() {
        ModEffects.getEffects().forEach((id, effect) ->
                Registry.register(BuiltInRegistries.MOB_EFFECT, id, effect)
        );
    }
}
