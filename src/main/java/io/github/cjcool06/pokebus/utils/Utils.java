package io.github.cjcool06.pokebus.utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.obj.Bus;
import io.github.cjcool06.pokebus.obj.Destination;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static EntityPixelmon getFromStatue(EntityStatue statue) {
        PokemonSpec spec = new PokemonSpec();
        spec.growth = (byte)statue.getGrowth().index;
        spec.boss = (byte)statue.getBossMode().index;
        spec.form = (byte)statue.getForm();
        Pokemon pokemon = spec.create();

        return pokemon.getOrMakeEntity();
    }

    public static Destination getNextVisibleDestination(Bus bus) {
        for (Destination destination : bus.getDestinationsPending()) {
            if (!destination.isGhost) {
                return destination;
            }
        }

        return bus.getDestinationsPending().isEmpty() ? null : bus.getDestinationsPending().get(bus.getDestinationsPending().size() - 1);
    }

    public static String toPlain(String busName) {
        return TextSerializers.FORMATTING_CODE.stripCodes(busName);
    }

    public static String getRandomName(ArrayList<String> names) {
        return names.get(new Random().nextInt(names.size()));
    }

    public static String getRandomNameFromConfig() {
        List<String> names = PokeBusConfig.driverNames;
        return names.isEmpty() ? "Null" : names.get(new Random().nextInt(names.size()));
    }

    public static Text getPrefix() {
        return TextSerializers.FORMATTING_CODE.deserialize(PokeBusConfig.prefix);
    }

    public static void sendMessage(Player player, Text text) {
        player.sendMessage(Text.of(getPrefix(), text));
    }
}
