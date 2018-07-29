package io.github.cjcool06.pokebus.listeners;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BattleStartedListener {

    @SubscribeEvent
    public void battleStarted(BattleStartedEvent event) {
        if (!event.bc.isPvP()) {
            if (event.participant1[0].getEntity() instanceof EntityPixelmon) {
                EntityPixelmon pixelmon = (EntityPixelmon)event.participant1[0].getEntity();
                if (pixelmon.getEntityData().hasKey("IsBus")) {
                    event.setCanceled(true);
                }
            }
            else if (event.participant2[0].getEntity() instanceof EntityPixelmon) {
                EntityPixelmon pixelmon = (EntityPixelmon)event.participant2[0].getEntity();
                if (pixelmon.getEntityData().hasKey("IsBus")) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
