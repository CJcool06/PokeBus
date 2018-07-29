package io.github.cjcool06.pokebus.mixins;

import io.github.cjcool06.pokebus.config.PokeBusConfig;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PathNavigate.class)
public abstract class MixinPathNavigate {
    @Shadow
    protected EntityLiving entity;

    @Inject(method = "getPathSearchRange", at = @At("HEAD"), cancellable = true)
    protected void onGetPathSearchRange(CallbackInfoReturnable<Float> cir) {
        if (this.entity.getEntityData().hasKey("IsBusDriver")) {
            cir.setReturnValue((float)PokeBusConfig.maxDistance);
        }
    }
}
