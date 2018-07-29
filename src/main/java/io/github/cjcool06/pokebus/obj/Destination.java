package io.github.cjcool06.pokebus.obj;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.entity.living.player.Player;

public class Destination {
    private final String name;
    private final BlockPos blockPos;
    public boolean isGhost = false;

    public Destination(String name, BlockPos blockPos) {
        this.name = name;
        this.blockPos = blockPos;
    }

    public String getName() {
        return name;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void sendPlayer(Player player) {
        ((EntityPlayerMP)player).setPositionAndUpdate(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
    }
}
