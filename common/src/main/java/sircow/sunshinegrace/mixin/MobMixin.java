package sircow.sunshinegrace.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.sunshinegrace.effect.ModEffects;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void sunshinegrace$preventTargetWithSunshineGrace(LivingEntity target, CallbackInfo ci) {
        if (target instanceof Player player) {
            if (player.hasEffect(ModEffects.SUNSHINE_GRACE.holder)) {
                if (player.getY() >= 60.0D) ci.cancel();
            }
        }
    }
}
