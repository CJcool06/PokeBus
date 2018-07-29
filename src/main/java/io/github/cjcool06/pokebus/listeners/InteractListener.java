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

import java.util.ArrayList;

public class InteractListener {
    private final ArrayList<Player> playersFalling = new ArrayList<>();

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
                        player.sendMessage(Text.of(TextColors.GREEN, "Created new PokeBus Stop: ", TextColors.AQUA, name));
                        ChatListener.listeningForInteract.remove(player);
                        StopsCommand.showStopsSummary(player);
                    }
                    else {
                        player.sendMessage(Text.of(TextColors.RED, "That statue is already a PokeBus Stop."));
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
                        player.sendMessage(Text.of(TextColors.RED, "This PokeBus stop is currently inactive. Check back later!"));
                    }
                    else if (busStop.getDestinations().isEmpty()) {
                        player.sendMessage(Text.of(TextColors.RED, "This PokeBus Stop hasn't any destinations."));
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

    // TODO: Easter egg when I can be bothered to get it working.
    /*
    @Listener
    public void onBusInteract(InteractEntityEvent.Primary event, @Root DamageSource source) {
        Entity entity = (Entity)event.getTargetEntity();
        if (entity.getEntityData().hasKey("IsBus") || entity.getEntityData().hasKey("IsBusDriver")) {
            if (source instanceof EntityDamageSource) {
                EntityDamageSource eDmg = (EntityDamageSource)source;
                if (eDmg.getSource() instanceof Player) {
                    Player player = (Player)eDmg.getSource();
                    player.sendMessage(Text.of("1"));
                    if (!player.getVehicle().isPresent()) {
                        player.sendMessage(Text.of("2"));
                        event.setCancelled(true);
                        player.setVelocity(player.getVelocity().add(0,0,8));
                        if (!playersFalling.contains(player)) {
                            playersFalling.add(player);
                            Sponge.getScheduler().createTaskBuilder().execute(() -> playersFalling.remove(player)).delay(30, TimeUnit.SECONDS).submit(PokeBus.getPlugin());
                        }
                    }
                }
            }
        }
    }

    @Listener
    public void onDamage(DamageEntityEvent event, @Root DamageSource source, @Getter("getTargetEntity") Player player) {
        if (playersFalling.contains(player) && source.getType().equals(DamageTypes.FALL)) {
            playersFalling.remove(player);
            event.setCancelled(true);
        }
    }*/
}
