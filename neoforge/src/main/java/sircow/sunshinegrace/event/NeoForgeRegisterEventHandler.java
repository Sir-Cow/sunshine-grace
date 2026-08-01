package sircow.sunshinegrace.event;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.effect.ModEffects;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeRegisterEventHandler {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.MOB_EFFECT, helper ->
                ModEffects.getEffects().forEach(helper::register)
        );
    }
}
