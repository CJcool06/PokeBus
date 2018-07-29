package io.github.cjcool06.pokebus.ai;

import com.flowpowered.math.vector.Vector3d;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.obj.Bus;
import io.github.cjcool06.pokebus.obj.Destination;
import io.github.cjcool06.pokebus.utils.Utils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

public class BusAI extends EntityAIBase {
    private final Bus bus;
    private final double speed;
    private Destination currentDestination = null;
    private Destination prevDestination = null;
    private int timeToRecalcPath = 40;
    private int errorCounter = 0;
    private boolean canSendUpdate = true;

    public BusAI(Bus bus, double speed) {
        this.bus = bus;
        this.speed = speed;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (!canExecute()) {
            bus.retire();
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!canExecute()) {
            bus.retire();
            return false;
        }
        if (currentDestination != null) {
            double distSq = ((Entity)bus.getPokemon()).getLocation().getPosition().distanceSquared(new Vector3d(currentDestination.getBlockPos().getX(), currentDestination.getBlockPos().getY(), currentDestination.getBlockPos().getZ()));
            if (distSq <= 4) {
                prevDestination = currentDestination;
                bus.getDestinationsPending().remove(currentDestination);
                if (bus.getDestinationsPending().isEmpty()) {
                    bus.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(PokeBusConfig.rideEnd));
                    bus.getPokemon().removePassengers();
                    bus.retire();
                }
                else {
                    if (!currentDestination.isGhost) {
                        bus.sendMessage(Text.of(TextColors.GREEN, "Reached destination ", TextColors.LIGHT_PURPLE, prevDestination.getName(), TextColors.GREEN, "."));
                        bus.sendMessage(Text.of(TextColors.GOLD, "Moving to the next destination in just a moment."));
                        this.timeToRecalcPath = 100;
                    }
                    currentDestination = bus.getDestinationsPending().get(0);
                    canSendUpdate = true;
                }
            }
        }
        return canExecute();
    }

    @Override
    public boolean isInterruptible() {
        return true;
    }

    @Override
    public void startExecuting() {
    }

    @Override
    public void updateTask() {
        EntityLiving driver = (EntityLiving)bus.getDriver();
        if (--this.timeToRecalcPath <= 0 && driver.getNavigator().noPath()) {
            currentDestination = bus.getDestinationsPending().get(0);
            Destination nextNotGhost = Utils.getNextVisibleDestination(bus);

            if (driver.getNavigator().tryMoveToXYZ(currentDestination.getBlockPos().getX(), currentDestination.getBlockPos().getY(), currentDestination.getBlockPos().getZ(), speed)) {
                if ((prevDestination == null || !prevDestination.isGhost) && canSendUpdate) {
                    bus.sendMessage(Text.of(TextColors.GOLD, "Moving to destination ", TextColors.LIGHT_PURPLE, nextNotGhost.getName(), TextColors.GOLD, "."));
                    canSendUpdate = false;
                }
                errorCounter = 0;
            }
            else {
                if (errorCounter == 0) {
                    bus.sendMessage(Text.of(TextColors.RED, "There was a problem trying to get to destination ", TextColors.GOLD, currentDestination.getName(), TextColors.RED, ". Retrying..."));
                }
                else if (errorCounter == 100) {
                    bus.sendMessage(Text.builder().append(Text.of(TextColors.RED, "It looks like we've been stuck for over 5 seconds. Feel free to click here to be teleported to the next destination."))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                bus.retire();
                                nextNotGhost.sendPlayer(bus.getPassenger());
                                bus.sendMessage(Text.of(TextColors.GOLD, "You've been teleported to destination " + nextNotGhost.getName()));
                            }))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click here to teleported to the next destination")))
                    .build());
                }
                errorCounter++;
            }
        }
    }

    @Override
    public void resetTask() {
        bus.getPokemon().getNavigator().clearPath();
    }

    private boolean canExecute() {
        return !bus.getDestinationsPending().isEmpty() && !bus.getDriver().getPassengers().isEmpty() && !bus.getDriver().isRemoved() && bus.getPokemon().isEntityAlive();
    }
}
