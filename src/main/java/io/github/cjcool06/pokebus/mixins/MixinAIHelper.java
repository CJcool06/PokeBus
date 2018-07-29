package io.github.cjcool06.pokebus.mixins;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.AIHelper;
import net.minecraft.entity.ai.EntityAITasks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AIHelper.class, remap = false)
public class MixinAIHelper {
    @Inject(method = "setWanderGroundAI", at = @At("HEAD"), cancellable = true)
    protected void onSetWanderGround(EntityPixelmon entity, EntityAITasks tasks, CallbackInfo ci) {
        if (entity.getEntityData().hasKey("IsBus")) {
            ci.cancel();
        }
    }
}
