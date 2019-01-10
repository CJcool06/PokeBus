package io.github.cjcool06.pokebus.managers;

import com.google.gson.*;
import io.github.cjcool06.pokebus.PokeBus;
import io.github.cjcool06.pokebus.obj.BusStop;
import io.github.cjcool06.pokebus.obj.Destination;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.Sponge;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class DataManager {
    private static File dir = new File("config/pokebus/stops");
    public static void save() {
        // Creates dir if there isn't one
        dir.mkdirs();

        for (BusStop busStop : BusManager.getBusStops()) {
            File file = new File(dir, busStop.getName() + ".json");

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                JsonObject obj = new JsonObject();
                JsonArray destinations = new JsonArray();
                JsonArray driverNames = new JsonArray();

                for (Destination destination : busStop.getDestinations()) {
                    JsonObject destObj = new JsonObject();

                    destObj.add("Name", new JsonPrimitive(destination.getName()));
                    destObj.add("IsGhost", new JsonPrimitive(destination.isGhost));
                    destObj.add("PosX", new JsonPrimitive(destination.getBlockPos().getX()));
                    destObj.add("PosY", new JsonPrimitive(destination.getBlockPos().getY()));
                    destObj.add("PosZ", new JsonPrimitive(destination.getBlockPos().getZ()));
                    destinations.add(destObj);
                }
                for (String name : busStop.getDriverNames()) {
                    driverNames.add(new JsonPrimitive(name));
                }

                obj.add("StatueUID", new JsonPrimitive(busStop.getStatueUID().toString()));
                obj.add("WorldUID", new JsonPrimitive(busStop.getWorld().getUniqueId().toString()));
                obj.add("Name", new JsonPrimitive(busStop.getName()));
                obj.add("TravelSpeed", new JsonPrimitive(busStop.getTravelSpeed()));
                obj.add("Destinations", destinations);
                obj.add("DriverNames", driverNames);

                PrintWriter pw = new PrintWriter(file);
                String objString = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
                pw.print(objString);
                pw.flush();
                pw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void load() {
        // Creates dir if there isn't one
        dir.mkdirs();
        JsonParser parser = new JsonParser();

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                try {
                    JsonObject jsonObject = (JsonObject)parser.parse(new FileReader(file));
                    ArrayList<Destination> destinations = new ArrayList<>();
                    ArrayList<String> driverNames = new ArrayList<>();

                    UUID statueUID = UUID.fromString(jsonObject.get("StatueUID").getAsString());
                    UUID worldUID = UUID.fromString(jsonObject.get("WorldUID").getAsString());
                    String name = jsonObject.get("Name").getAsString();

                    for (JsonElement element : jsonObject.get("Destinations").getAsJsonArray()) {
                        String destName = element.getAsJsonObject().get("Name").getAsString();
                        boolean isGhost = element.getAsJsonObject().get("IsGhost").getAsBoolean();
                        int posX = element.getAsJsonObject().get("PosX").getAsInt();
                        int posY = element.getAsJsonObject().get("PosY").getAsInt();
                        int posZ = element.getAsJsonObject().get("PosZ").getAsInt();
                        BlockPos blockPos = new BlockPos(posX, posY, posZ);
                        Destination destination = new Destination(destName, blockPos);
                        destination.isGhost = isGhost;
                        destinations.add(destination);
                    }
                    for (JsonElement element : jsonObject.get("DriverNames").getAsJsonArray()) {
                        driverNames.add(element.getAsString());
                    }

                    if (!Sponge.getServer().getWorld(worldUID).isPresent()) {
                        PokeBus.getLogger().warn("Error deserializing PokeBus Stop " + name + ": World not found.");
                        continue;
                    }
                    BusStop busStop = new BusStop(statueUID, Sponge.getServer().getWorld(worldUID).get(), name, driverNames);
                    busStop.getDestinations().addAll(destinations);
                    busStop.setTravelSpeed(jsonObject.get("TravelSpeed").getAsDouble());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public static void deleteFile(BusStop busStop) {
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.getName().equalsIgnoreCase(busStop.getName() + ".json")) {
                    file.delete();
                }
            }
        }
    }
}
