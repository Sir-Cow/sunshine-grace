package sircow.sunshinegrace.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.effect.custom.SunshineGraceEffect;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModEffects {
    private static final Map<Identifier, MobEffect> EFFECTS = new LinkedHashMap<>();

    public static final MobEffect SUNSHINE_GRACE = register("sunshine_grace", new SunshineGraceEffect());

    private static MobEffect register(String name, MobEffect effect) {
        EFFECTS.put(Constants.id(name), effect);
        return effect;
    }

    public static Holder.Reference<MobEffect> sunshineGraceHolder() {
        return BuiltInRegistries.MOB_EFFECT.get(Constants.id("sunshine_grace")).orElseThrow();
    }

    public static Map<Identifier, MobEffect> getEffects() {
        return EFFECTS;
    }
}
