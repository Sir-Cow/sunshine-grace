package sircow.sunshinegrace.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.effect.ModEffects;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeModEvents {
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;

        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            if (player.hasEffect(ModEffects.sunshineGraceHolder())) {
                player.removeEffect(ModEffects.sunshineGraceHolder());
            }
        }
    }
}
