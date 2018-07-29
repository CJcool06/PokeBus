package io.github.cjcool06.pokebus.listeners;

import com.pixelmonmod.pixelmon.api.events.StatueEvent;
import io.github.cjcool06.pokebus.managers.BusManager;
import io.github.cjcool06.pokebus.obj.BusStop;
import io.github.cjcool06.pokebus.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class StatueListener {

    @SubscribeEvent
    public void onDestroy(StatueEvent.DestroyStatue event) {
        BusStop busStop = BusManager.getBusStop(event.statue);
        if (busStop != null) {
            BusManager.removeBusStop(busStop.getName());
            Utils.sendMessage((Player)event.player, Text.of(TextColors.RED, "Removed PokeBus Stop ", TextColors.AQUA, busStop.getName(), TextColors.RED, "."));
        }
    }
}
