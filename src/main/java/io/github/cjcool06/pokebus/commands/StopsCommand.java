package io.github.cjcool06.pokebus.commands;

import io.github.cjcool06.pokebus.listeners.ChatListener;
import io.github.cjcool06.pokebus.managers.BusManager;
import io.github.cjcool06.pokebus.obj.BusStop;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;

public class StopsCommand implements CommandExecutor {

    public static CommandSpec getSpec() {
        return CommandSpec.builder()
                .description(Text.of("Edit PokeBus stops"))
                .permission("pokebus.admin.stops")
                .executor(new StopsCommand())
                .build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) {
        showStopsSummary(src);
        return CommandResult.success();
    }

    public static void showStopsSummary(CommandSource src) {
        ArrayList<Text> contents = new ArrayList<>();

        for (BusStop busStop : BusManager.getBusStops()) {
            contents.add(Text.builder()
                    .append(Text.builder().append(Text.of(TextColors.RED, "[", TextColors.DARK_RED, "-", TextColors.RED, "]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to delete this PokeBus stop")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                BusManager.removeBusStop(busStop);
                                showStopsSummary(src);
                            }))
                            .build())
                    .append(Text.builder().append(Text.of(TextColors.DARK_AQUA, " [", TextColors.AQUA, busStop.getName(), TextColors.DARK_AQUA, "]", TextColors.GRAY, " -"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to teleport to this PokeBus stop")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                busStop.sendPlayer((Player)src);
                                src.sendMessage(Text.of(TextColors.GRAY, "Teleported to PokeBus stop ", TextColors.AQUA, busStop.getName(), TextColors.GRAY, "."));
                            }))
                            .build())
                    .append(Text.builder().append(Text.of(TextColors.DARK_PURPLE, " [", TextColors.LIGHT_PURPLE, "Destinations", TextColors.DARK_PURPLE, "]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to edit the destinations this bus will travel to")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                busStop.editDestinations((Player)src);
                            }))
                            .build())
                    .append(Text.builder().append(Text.of(TextColors.DARK_GREEN, " [Driver Names]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to edit this PokeBus stop's driver names")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                busStop.editNames(src);
                            })).build())
                    .append(Text.builder().append(Text.of(TextColors.GOLD, " [", TextColors.YELLOW, "Other", TextColors.GOLD, "]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to edit bus variables for this PokeBus stop")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                busStop.editOther(src);
                            }))
                            .build())
                    .build()
            );
        }

        contents.add(Text.of(
                TextActions.executeCallback(dummySrc -> {
                    ChatListener.clearListeners((Player)src);
                    ChatListener.listeningForChatStopsCommand.add((Player)src);
                    src.sendMessage(Text.of(TextColors.GRAY, "What is the name of this PokeBus stop?"));
                }),
                TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to create a new PokeBus stop")),
                TextColors.GREEN, "[+]"
        ));

        PaginationList.builder()
                .title(Text.of(TextColors.AQUA, " PokeBus Stops "))
                .contents(contents)
                .padding(Text.of(TextColors.GRAY, "-", TextColors.RESET))
                .sendTo(src);
    }
}
