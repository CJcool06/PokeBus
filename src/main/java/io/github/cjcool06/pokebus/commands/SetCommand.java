package io.github.cjcool06.pokebus.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;

public class SetCommand implements CommandExecutor {

    public static CommandSpec getSpec() {
        return CommandSpec.builder()
                .description(Text.of("Set a new poke-bus stop"))
                .permission("safetrade.admin.set")
                .executor(new SetCommand())
                .build();
    }

    public static HashMap<Player, Boolean> listeningFor = new HashMap<>();

    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player)src;
            RemoveCommand.listeningFor.removeIf(player1 -> player1.equals(player));
            listeningFor.put(player, false);
            player.sendMessage(Text.of(TextColors.GRAY, "Right click a statue to get started."));
        }
        else {
            src.sendMessage(Text.of(TextColors.RED, "You must have hands to manipulate poke-bus stops."));
        }

        return CommandResult.success();
    }
}
