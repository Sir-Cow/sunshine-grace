package sircow.sunshinegrace.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.effect.ModEffects;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeModEvents {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;

        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            if (player.hasEffect(ModEffects.sunshineGraceHolder())) {
                player.removeEffect(ModEffects.sunshineGraceHolder());
            }
        }
    }
}
