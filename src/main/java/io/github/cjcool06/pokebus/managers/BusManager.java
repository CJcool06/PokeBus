package io.github.cjcool06.pokebus.managers;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.obj.Bus;
import io.github.cjcool06.pokebus.obj.BusStop;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;

public class BusManager {
    private static ArrayList<BusStop> busStops = new ArrayList<>();

    public static ArrayList<BusStop> getBusStops() {
        return new ArrayList<>(busStops);
    }

    public static void registerBusStop(BusStop busStop) {
        busStops.add(busStop);
    }

    public static void removeBusStop(String name) {
        for (BusStop busStop : busStops) {
            if (busStop.getName().equalsIgnoreCase(name)) {
                removeBusStop(busStop);
                break;
            }
        }
    }

    public static void removeBusStop(BusStop busStop) {
        busStops.remove(busStop);
        DataManager.deleteFile(busStop);
    }

    public static BusStop getBusStop(EntityStatue statue) {
        for (BusStop busStop : busStops) {
            if (busStop.getStatueUID().equals(statue.getUniqueID())) {
                return busStop;
            }
            /*
            if (busStop.getStatue().getPosition().equals(statue.getPosition())) {
                return busStop;
            }*/
        }

        return null;
    }

    public static BusStop getBusStop(String name) {
        for (BusStop busStop : busStops) {
            if (name.equalsIgnoreCase(busStop.getName())) {
                return busStop;
            }
        }

        return null;
    }

    public static boolean isNameTaken(String name) {
        for (BusStop busStop : busStops) {
            if (busStop.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBusStop(EntityStatue statue) {
        for (BusStop busStop : busStops) {
            if (busStop.getStatueUID().equals(statue.getUniqueID())) {
                return true;
            }
            /*
            if (busStop.getStatue().getPosition().equals(statue.getPosition())) {
                return true;
            }*/
        }

        return false;
    }

    public static Bus getBusOfPlayer(Player player) {
        for (BusStop busStop : busStops) {
            for (Bus bus : busStop.getBuses()) {
                if (bus.getPassenger().equals(player)) {
                    return bus;
                }
            }
        }

        return null;
    }
}
