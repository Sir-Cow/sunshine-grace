package sircow.sunshinegrace.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.effect.custom.SunshineGraceEffect;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModEffects {
    private static final Map<ResourceLocation, MobEffect> EFFECTS = new LinkedHashMap<>();

    public static final MobEffect SUNSHINE_GRACE = register("sunshine_grace", new SunshineGraceEffect());

    private static MobEffect register(String name, MobEffect effect) {
        EFFECTS.put(Constants.id(name), effect);
        return effect;
    }

    public static Map<ResourceLocation, MobEffect> getEffects() {
        return EFFECTS;
    }
}
