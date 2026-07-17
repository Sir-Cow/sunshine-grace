package sircow.sunshinegrace.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sircow.sunshinegrace.Constants;

import java.util.HashMap;
import java.util.Map;

public class ForgeModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(
            ForgeRegistries.MOB_EFFECTS,
            Constants.MOD_ID
    );

    private static final Map<ModEffects.EffectEntry, RegistryObject<MobEffect>> registryMap = new HashMap<>();

    public static void init(BusGroup modBusGroup) {
        registerForgeModEffects();
        EFFECTS.register(modBusGroup);
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(ForgeModEffects::commonSetup);
    }

    public static void registerForgeModEffects() {
        for (ModEffects.EffectEntry effect : ModEffects.ALL_EFFECTS) {
            registryMap.put(effect, EFFECTS.register(effect.id, effect.factory));
        }
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ForgeModEffects::assignToCommon);
    }

    public static void assignToCommon() {
        for (Map.Entry<ModEffects.EffectEntry, RegistryObject<MobEffect>> entry : registryMap.entrySet()) {
            entry.getKey().holder = entry.getValue().getHolder().orElseThrow();
        }
    }
}
