package io.github.cjcool06.pokebus.commands;

import io.github.cjcool06.pokebus.config.PokeBusConfig;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadCommand implements CommandExecutor {

    public static CommandSpec getSpec() {
        return CommandSpec.builder()
                .description(Text.of("Reloads config"))
                .permission("pokebus.admin.reload")
                .executor(new ReloadCommand())
                .build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) {
        src.sendMessage(Text.of(TextColors.GRAY, "Attempting to reload config..."));
        PokeBusConfig.load();
        src.sendMessage(Text.of(TextColors.GRAY, "Config reloaded."));
        return CommandResult.success();
    }
}
