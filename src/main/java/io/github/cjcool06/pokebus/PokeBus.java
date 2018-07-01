package io.github.cjcool06.pokebus;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.cjcool06.pokebus.commands.HelpCommand;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.listeners.InteractListener;
import io.github.cjcool06.pokebus.listeners.StatueListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
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
    public static final String VERSION = "1.0.0";
    public static final String DESCRIPTION = "Treat pokemon more like slaves than they already are";
    public static final String AUTHORS = "CJcool06";
    private static PokeBus plugin;

    @Inject
    private Logger logger;

    @Listener
    public void onInit(GameInitializationEvent event) {
        plugin = this;
        PokeBusConfig.load();

        Sponge.getEventManager().registerListeners(this, new InteractListener());
        Pixelmon.EVENT_BUS.register(new StatueListener());

        CommandSpec pokeBus = CommandSpec.builder()
                .description(Text.of("Base command"))
                .permission("pokebus.common.help")
                .executor(new HelpCommand())
                .build();
    }

    public static PokeBus getPlugin() {
        return plugin;
    }

    public static Logger getLogger() {
        return getPlugin().logger;
    }
}
