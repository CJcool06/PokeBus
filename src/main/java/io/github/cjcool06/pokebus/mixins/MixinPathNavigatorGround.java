package io.github.cjcool06.pokebus.mixins;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PathNavigateGround.class)
public abstract class MixinPathNavigatorGround extends PathNavigate {

    public MixinPathNavigatorGround(EntityLiving p_i1671_1_, World p_i1671_2_) {
        super(p_i1671_1_, p_i1671_2_);
    }

    @Inject(method = "canNavigate", at = @At("HEAD"), cancellable = true)
    protected void onCanNavigate(CallbackInfoReturnable<Boolean> cir) {
        if (this.entity.getEntityData().hasKey("IsBus")) {
            cir.setReturnValue(true);
        }
    }
}
