package io.github.cjcool06.pokebus.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor {

    public CommandResult execute(CommandSource src, CommandContext args) {
        List<Text> contents = new ArrayList<>();

        contents.add(Text.of(TextColors.AQUA, "/pokebus set", TextColors.GRAY, " - ", TextColors.GRAY, "Set a new poke-bus stop"));
        contents.add(Text.of(TextColors.AQUA, "/pokebus remove", TextColors.GRAY, " - ", TextColors.GRAY, "Remove a poke-bus stop"));

        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, " PokeBus "))
                .contents(contents)
                .padding(Text.of(TextColors.GRAY, "-", TextColors.RESET))
                .sendTo(src);

        return CommandResult.success();
    }
}
