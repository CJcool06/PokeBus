package io.github.cjcool06.pokebus;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.cjcool06.pokebus.commands.HelpCommand;
import io.github.cjcool06.pokebus.commands.ReloadCommand;
import io.github.cjcool06.pokebus.commands.StopsCommand;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.listeners.*;
import io.github.cjcool06.pokebus.managers.DataManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(id = PokeBus.ID,
        name = PokeBus.NAME,
        version = PokeBus.VERSION,
        description = PokeBus.DESCRIPTION,
        authors = PokeBus.AUTHORS,
        dependencies = @Dependency(id = "pixelmon")
)
public class PokeBus {
    public static final String ID = "pokebus";
    public static final String NAME = "PokeBus";
    public static final String VERSION = "1.0.6";
    public static final String DESCRIPTION = "Treat pokemon more like slaves than they already are";
    public static final String AUTHORS = "CJcool06";
    private static PokeBus plugin;

    @Inject
    private Logger logger;

    @Listener
    public void onInit(GameInitializationEvent event) {
        plugin = this;
        logger.info("Developed by CJcool06");
        logger.info("Loading config.");
        PokeBusConfig.load();

        Sponge.getEventManager().registerListeners(this, new InteractListener());
        Sponge.getEventManager().registerListeners(this, new ChatListener());
        Pixelmon.EVENT_BUS.register(new StatueListener());
        Pixelmon.EVENT_BUS.register(new CatchListener());
        Pixelmon.EVENT_BUS.register(new BattleStartedListener());

        CommandSpec pokeBus = CommandSpec.builder()
                .description(Text.of("Base command"))
                .permission("pokebus.admin.help")
                .executor(new HelpCommand())
                .child(StopsCommand.getSpec(), "stops")
                .child(ReloadCommand.getSpec(), "reload")
                .build();
        Sponge.getCommandManager().register(this, pokeBus, "pokebus", "pbus");
    }

    @Listener
    public void onStart(GameStartedServerEvent event) {
        logger.info("Loading data.");
        DataManager.load();
    }

    @Listener
    public void onStopping(GameStoppingServerEvent event) {
        logger.info("Saving data.");
        DataManager.save();
    }

    public static PokeBus getPlugin() {
        return plugin;
    }

    public static Logger getLogger() {
        return getPlugin().logger;
    }
}
