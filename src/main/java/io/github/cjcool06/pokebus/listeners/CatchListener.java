package io.github.cjcool06.pokebus.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CatchListener {

    @SubscribeEvent
    public void onCatchStart(CaptureEvent.StartCapture event) {
        Player player = (Player)event.player;
        if (event.getPokemon().getEntityData().hasKey("IsBus")) {
            player.sendMessage(Text.of(TextColors.RED, "You cannot catch a PokeBus!"));
            event.setCanceled(true);
        }
    }
}
