package io.github.cjcool06.pokebus.listeners;

import io.github.cjcool06.pokebus.managers.BusManager;
import io.github.cjcool06.pokebus.obj.BusStop;
import io.github.cjcool06.pokebus.obj.Destination;
import io.github.cjcool06.pokebus.utils.Utils;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListener {
    public static ArrayList<Player> listeningForChatStopsCommand = new ArrayList<>();
    public static HashMap<Player, BusStop> listeningForChatBusStops = new HashMap<>();
    public static HashMap<Player, String> listeningForInteract = new HashMap<>();
    public static HashMap<Player, BusStop> listeningForName = new HashMap<>();

    @Listener(order = Order.FIRST)
    public void onChat(MessageChannelEvent event) {
        if (event.getSource() instanceof Player) {
            Player player = (Player)event.getSource();
            String message = event.getMessage().toPlain();
            // messageArrgs[0] is the player name
            String[] messageArgs = message.split(" ");

            if (listeningForChatStopsCommand.contains(player) || listeningForChatBusStops.containsKey(player)) {
                if (messageArgs.length > 2 || messageArgs[1].length() > 10) {
                    player.sendMessage(Text.of(TextColors.RED, "The name must be 1 word and be no more than 10 characters long."));
                }
                else if (BusManager.isNameTaken(messageArgs[1])) {
                    player.sendMessage(Text.of(TextColors.RED, "That name is already being used by another PokeBus stop."));
                }
                else {
                    if (listeningForChatStopsCommand.contains(player)) {
                        listeningForInteract.put(player, messageArgs[1]);
                        listeningForChatStopsCommand.remove(player);
                        player.sendMessage(Text.of(TextColors.GRAY, "Right-click a statue to create the ", TextColors.AQUA, messageArgs[1], TextColors.GRAY, " PokeBus stop."));
                    }
                    else if (listeningForChatBusStops.containsKey(player)) {
                        BusStop busStop = listeningForChatBusStops.get(player);
                        Destination destination = new Destination(messageArgs[1], ((EntityPlayerMP)player).getPosition());
                        busStop.getDestinations().add(destination);
                        busStop.editDestinations(player);
                        listeningForChatBusStops.remove(player);
                        player.sendMessage(Text.of(TextColors.GREEN, "Created new destination ", TextColors.LIGHT_PURPLE, destination.getName(), TextColors.GREEN,
                                " for PokeBus stop ", TextColors.AQUA, busStop.getName(), TextColors.GREEN, "."));
                    }
                }
                event.setMessageCancelled(true);
            }
            else if (listeningForName.containsKey(player)) {
                BusStop busStop = listeningForName.get(player);
                if (messageArgs.length > 2) {
                    player.sendMessage(Text.of(TextColors.RED, "The name must be 1 word."));
                }
                else {
                    for (String name : busStop.getDriverNames()) {
                        if (Utils.toPlain(name).equalsIgnoreCase(Utils.toPlain(messageArgs[1]))) {
                            player.sendMessage(Text.of(TextColors.RED, "That name is already being used by this PokeBus stop."));
                            event.setMessageCancelled(true);
                            return;
                        }
                    }
                    busStop.getDriverNames().add(messageArgs[1]);
                    listeningForName.remove(player);
                }
                event.setMessageCancelled(true);
            }
        }
    }

    public static void clearListeners(Player player) {
        listeningForChatStopsCommand.remove(player);
        listeningForChatBusStops.remove(player);
        listeningForInteract.remove(player);
        listeningForName.remove(player);
    }
}
