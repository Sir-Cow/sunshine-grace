package sircow.sunshinegrace.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.sunshinegrace.effect.ModEffects;
import sircow.sunshinegrace.other.FirstJoinTracker;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements FirstJoinTracker {
    @Unique private boolean hasCheckedFirstJoin = false;
    @Unique private Optional<Boolean> hasJoinedBefore = Optional.empty();

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }
    @Override
    public Optional<Boolean> sunshinegrace$getHasJoinedBefore() {
        return hasJoinedBefore;
    }

    @Override
    public void sunshinegrace$setHasJoinedBefore(Optional<Boolean> value) {
        this.hasJoinedBefore = value;
    }

    @Override
    public boolean sunshinegrace$getHasCheckedFirstJoin() {
        return hasCheckedFirstJoin;
    }

    @Override
    public void sunshinegrace$setHasCheckedFirstJoin(boolean value) {
        this.hasCheckedFirstJoin = value;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void sunshinegrace$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("sunshinegrace:hasJoinedBefore", hasJoinedBefore.orElse(false));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void sunshinegrace$readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        boolean joinedBefore = compoundTag.getBoolean("sunshinegrace:hasJoinedBefore");
        this.hasJoinedBefore = Optional.of(joinedBefore);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void sunshinegrace$copyFirstJoinState(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {
        if (oldPlayer instanceof FirstJoinTracker oldTracker) {
            FirstJoinTracker newTracker = this;
            newTracker.sunshinegrace$setHasJoinedBefore(oldTracker.sunshinegrace$getHasJoinedBefore());
            newTracker.sunshinegrace$setHasCheckedFirstJoin(oldTracker.sunshinegrace$getHasCheckedFirstJoin());
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void sunshinegrace$onTick(CallbackInfo ci) {
        if (!hasCheckedFirstJoin) {
            hasCheckedFirstJoin = true;

            if (hasJoinedBefore.isEmpty()) hasJoinedBefore = Optional.of(false);

            if (!hasJoinedBefore.get()) {
                this.addEffect(new MobEffectInstance(ModEffects.sunshineGraceHolder(), 20 * 60 * 10, 0));
                hasJoinedBefore = Optional.of(true);
            }
        }
    }
}
