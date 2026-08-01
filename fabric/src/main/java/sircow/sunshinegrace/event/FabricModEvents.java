package sircow.sunshinegrace.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import sircow.sunshinegrace.effect.ModEffects;

public class FabricModEvents {
    public static void removeEffectWhenPlayerDamagesHostile() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof Monster)) return true;

            if (source.getEntity() instanceof ServerPlayer player) {
                if (player.hasEffect(ModEffects.sunshineGraceHolder())) {
                    player.removeEffect(ModEffects.sunshineGraceHolder());
                }
            }
            return true;
        });
    }

    public static void registerFabricModEvents() {
        removeEffectWhenPlayerDamagesHostile();
    }
}
