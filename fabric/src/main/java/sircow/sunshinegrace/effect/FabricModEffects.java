package sircow.sunshinegrace.effect;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.sunshinegrace.Constants;

public class FabricModEffects {
    public static void registerFabricModEffects() {
        register(ModEffects.SUNSHINE_GRACE);
    }

    private static void register(ModEffects.EffectEntry entry) {
        entry.holder = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT,
                Constants.id(entry.id),
                entry.factory.get()
        );
    }
}
