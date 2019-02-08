package io.github.cjcool06.pokebus.mixins;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity4Interactions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity4Interactions.class, remap = false)
public abstract class MixinEntity4Interactions extends Entity3HasStats {
    public MixinEntity4Interactions(World par1World) {
        super(par1World);
    }

    @Inject(method = "resetAI", at = @At("HEAD"), cancellable = true)
    protected void onResetAI(CallbackInfo ci) {
        if (this.getEntityData().hasKey("IsBus")) {
            ci.cancel();
        }
    }
}
