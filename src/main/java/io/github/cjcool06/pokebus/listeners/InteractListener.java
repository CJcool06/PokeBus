package io.github.cjcool06.pokebus.listeners;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.commands.StopsCommand;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.managers.BusManager;
import io.github.cjcool06.pokebus.obj.Bus;
import io.github.cjcool06.pokebus.obj.BusStop;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

public class InteractListener {

    @Listener
    public void onInteractEntity(InteractEntityEvent.Secondary event) {
        if (event.getSource() instanceof Player && event.getHandType().equals(HandTypes.MAIN_HAND)) {
            Player player = (Player)event.getSource();
            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() &&
                    player.getItemInHand(HandTypes.MAIN_HAND).get().getType().equals(Sponge.getRegistry().getType(ItemType.class, "pixelmon:chisel").get())) {
                return;
            }
            if (ChatListener.listeningForInteract.containsKey((Player)event.getSource())) {
                if (event.getTargetEntity() instanceof EntityStatue) {
                    EntityStatue statue = (EntityStatue)event.getTargetEntity();

                    if (!BusManager.isBusStop(statue)) {
                        String name = ChatListener.listeningForInteract.get(player);
                        new BusStop(statue, name);
                        player.sendMessage(Text.of(TextColors.GREEN, "Created new PokeBus stop: ", TextColors.AQUA, name));
                        ChatListener.listeningForInteract.remove(player);
                        StopsCommand.showStopsSummary(player);
                    }
                    else {
                        player.sendMessage(Text.of(TextColors.RED, "That statue is already a PokeBus stop."));
                    }
                }
                else {
                    player.sendMessage(Text.of(TextColors.RED, "You did not right-click a statue; cancelling tool."));
                    ChatListener.listeningForInteract.remove(player);
                    event.setCancelled(true);
                }
            }
            else if (event.getTargetEntity() instanceof EntityStatue) {
                EntityStatue statue = (EntityStatue)event.getTargetEntity();
                BusStop busStop = BusManager.getBusStop(statue);
                if (busStop != null) {
                    if (!busStop.isActive) {
                        player.sendMessage(Text.of(TextColors.RED, "This bus stop is currently inactive. Check back later!"));
                    }
                    else if (busStop.getDestinations().isEmpty()) {
                        player.sendMessage(Text.of(TextColors.RED, "This PokeBus stop hasn't any destinations."));
                    }
                    else {
                        Bus bus = new Bus(busStop, player);
                        busStop.getBuses().add(bus);
                        bus.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(PokeBusConfig.rideStart));
                    }
                }
            }
        }
    }

    @Listener
    public void onInteractBlock(InteractBlockEvent.Secondary event) {
        if (event.getSource() instanceof Player && event.getHandType().equals(HandTypes.MAIN_HAND)) {
            Player player = (Player) event.getSource();

            if (ChatListener.listeningForInteract.containsKey(player)) {
                player.sendMessage(Text.of(TextColors.RED, "You did not right-click a statue; cancelling tool."));
                ChatListener.listeningForInteract.remove(player);
                event.setCancelled(true);
            }
        }
    }
}
