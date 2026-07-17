package sircow.sunshinegrace.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import sircow.sunshinegrace.effect.custom.SunshineGraceEffect;

import java.util.List;
import java.util.function.Supplier;

public class ModEffects {
    public static class EffectEntry {
        public final String id;
        public final Supplier<MobEffect> factory;
        public Holder<MobEffect> holder;

        public EffectEntry(String id, Supplier<MobEffect> factory) {
            this.id = id;
            this.factory = factory;
        }
    }

    public static final EffectEntry SUNSHINE_GRACE = new EffectEntry("sunshine_grace", SunshineGraceEffect::new);

    public static final List<EffectEntry> ALL_EFFECTS = List.of(
            SUNSHINE_GRACE
    );
}
