package com.teampotato.retargetato.mixin;

import com.teampotato.retargetato.ReTargetato;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MixinMobEntity extends LivingEntity {
    protected MixinMobEntity(EntityType<? extends LivingEntity> type, ServerLevel level) {
        super(type, level);
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void injectSetTarget(LivingEntity entity, CallbackInfo ci) {
        ReTargetato.injectSetTarget(entity, ci, (Mob)(Object)this);
    }
}
