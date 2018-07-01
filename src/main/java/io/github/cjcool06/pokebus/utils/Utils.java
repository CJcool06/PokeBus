package io.github.cjcool06.pokebus.utils;

import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    public static EntityPixelmon getFromStatue(EntityStatue statue) {
        EntityPixelmon pixelmon = (EntityPixelmon)PixelmonEntityList.createEntityByName(statue.getPokemonName(), FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        pixelmon.setGrowth(statue.getGrowth());
        pixelmon.setBoss(statue.getBossMode());
        pixelmon.setForm(statue.getForm());

        return pixelmon;
    }

    public static String getRandomNameFromConfig() {
        ArrayList<String> names = PokeBusConfig.INSTANCE.driverNames;
        return names.isEmpty() ? "Null" : names.get(new Random().nextInt(names.size()));
    }

    public static Text getPrefix() {
        return TextSerializers.FORMATTING_CODE.deserialize(PokeBusConfig.INSTANCE.prefix);
    }

    public static void sendMessage(Player player, Text text) {
        player.sendMessage(Text.of(getPrefix(), text));
    }
}
