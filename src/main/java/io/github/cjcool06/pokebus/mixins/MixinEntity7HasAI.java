package io.github.cjcool06.pokebus.mixins;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6CanBattle;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity7HasAI;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity7HasAI.class, remap = false)
public abstract class MixinEntity7HasAI extends Entity6CanBattle {
    public MixinEntity7HasAI(World par1World) {
        super(par1World);
    }

    @Inject(method = "resetAI", at = @At("HEAD"), cancellable = true)
    protected void onResetAI(CallbackInfo ci) {
        if (this.getEntityData().hasKey("IsBus")) {
            ci.cancel();
        }
    }
}
