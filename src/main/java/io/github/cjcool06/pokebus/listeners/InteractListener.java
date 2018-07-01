package io.github.cjcool06.pokebus.listeners;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.commands.RemoveCommand;
import io.github.cjcool06.pokebus.commands.SetCommand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class InteractListener {

    @Listener
    public void onInteractEntity(InteractEntityEvent.Secondary event) {
        if (event.getSource() instanceof Player) {
            Player player = (Player)event.getSource();

            if (event.getTargetEntity() instanceof EntityStatue) {
                EntityStatue statue = (EntityStatue)event.getTargetEntity();
                player.sendMessage(Text.of(TextColors.GREEN, "Detected statue: " + statue.getPokemonName()));

                if (SetCommand.listeningFor.containsKey(player)) {
                    if (SetCommand.listeningFor.get(player)) {
                        player.sendMessage(Text.of(TextColors.RED, "You have already selected a statue. Select a destination for the poke-bus."));
                    }
                    else {
                        if (statue.getEntityData().hasKey("IsBusStop") && statue.getEntityData().getBoolean("IsBus")) {
                            player.sendMessage(Text.of(TextColors.RED, "That statue is already a poke-bus stop."));
                        }
                        else {
                            statue.getEntityData().setBoolean("IsBusStop", true);
                            //player.sendMessage(Text.of(TextColors.RED, "That statue now enslaves " + statue.getPokemonName() + "'s for public transport. I hope you're happy."));
                        }
                        SetCommand.listeningFor.put(player, true);
                    }
                }
                else if (RemoveCommand.listeningFor.contains(player)) {
                    if (statue.getEntityData().hasKey("IsBusStop") && statue.getEntityData().getBoolean("IsBus")) {
                        statue.getEntityData().setBoolean("IsBusStop", false);
                        player.sendMessage(Text.of(TextColors.RED, "That statue is no longer a poke-bus stop."));
                    }
                    else {
                        player.sendMessage(Text.of(TextColors.RED, "That statue isn't a poke-bus stop."));
                    }

                    RemoveCommand.listeningFor.remove(player);
                }
                else if (statue.getEntityData().hasKey("IsBusStop") && statue.getEntityData().getBoolean("IsBusStop")) {
                    // Execute bus stuff
                }
            }
            else if (SetCommand.listeningFor.containsKey(player)) {
                if (SetCommand.listeningFor.get(player)) {
                    player.sendMessage(Text.of(TextColors.RED, "You have already selected a statue. Select a destination for the poke-bus."));
                }
                else {
                    if (statue.getEntityData().hasKey("IsBusStop") && statue.getEntityData().getBoolean("IsBus")) {
                        player.sendMessage(Text.of(TextColors.RED, "That statue is already a poke-bus stop."));
                    }
                    else {
                        statue.getEntityData().setBoolean("IsBusStop", true);
                        //player.sendMessage(Text.of(TextColors.RED, "That statue now enslaves " + statue.getPokemonName() + "'s for public transport. I hope you're happy."));
                    }
                }
                SetCommand.listeningFor.remove(player);
            }
            else if (SetCommand.listeningFor.containsKey(player) || RemoveCommand.listeningFor.contains(player)) {
                player.sendMessage(Text.of(TextColors.RED, "You did not right-click a statue; cancelling tool."));
                SetCommand.listeningFor.remove(player);
                RemoveCommand.listeningFor.remove(player);
            }
        }
    }

    // Allows players to easily cancel their tool by right-clicking a block
    @Listener
    public void onInteractBlock(InteractBlockEvent event) {
        if (event.getSource() instanceof Player) {
            Player player = (Player) event.getSource();

            if (SetCommand.listeningFor.contains(player) || RemoveCommand.listeningFor.contains(player)) {
                player.sendMessage(Text.of(TextColors.RED, "You did not right-click a statue; cancelling tool."));
                SetCommand.listeningFor.remove(player);
                RemoveCommand.listeningFor.remove(player);
            }
        }
    }
}
