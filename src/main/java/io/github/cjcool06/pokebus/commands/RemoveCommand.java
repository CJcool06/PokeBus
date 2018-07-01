package io.github.cjcool06.pokebus.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand implements CommandExecutor {

    public static CommandSpec getSpec() {
        return CommandSpec.builder()
                .description(Text.of("Set a new poke-bus stop"))
                .permission("safetrade.admin.set")
                .executor(new SetCommand())
                .build();
    }

    public static List<Player> listeningFor = new ArrayList<>();

    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player)src;
            SetCommand.listeningFor.removeIf(player1 -> player1.equals(player));

            if (!listeningFor.contains(player)) {
                listeningFor.add(player);
            }
            player.sendMessage(Text.of(TextColors.GRAY, "Right click a statue to remove it as a poke-bus stop."));
        }
        else {
            src.sendMessage(Text.of(TextColors.RED, "You must have hands to manipulate poke-bus stops."));
        }

        return CommandResult.success();
    }
}
