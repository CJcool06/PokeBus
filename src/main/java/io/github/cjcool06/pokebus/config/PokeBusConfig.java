package io.github.cjcool06.pokebus.config;

import io.github.cjcool06.pokebus.PokeBus;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PokeBusConfig {
    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode node;

    public static String prefix = "&b[PokeBus] &r";
    public static Integer maxDistance = 50000;
    public static boolean makeDriverInvisible = false;
    public static List<String> driverNames = new ArrayList<>();
    public static String rideStart = "&bBuckle up and enjoy the ride!";
    public static String rideEnd = "&aWe've reached the final destination, everybody off!";


    public static void load() {
        File file = new File("config/pokebus/pokebus.conf");
        try {
            loader = HoconConfigurationLoader.builder().setFile(file).build();
            node = loader.load();
            if (!file.exists()) {
                driverNames.add("&a&lPika");
                save();
            }
            else {
                prefix = node.getNode("Prefix").getString(prefix);
                maxDistance = node.getNode("MaxDistance").getInt(maxDistance);
                makeDriverInvisible = node.getNode("MakeDriverInvisible").getBoolean(makeDriverInvisible);
                driverNames = node.getNode("DriverNames").getChildrenList().stream().map(e -> e.getString()).collect(Collectors.toList());
                rideStart = node.getNode("RideStart").getString(rideStart);
                rideEnd = node.getNode("RideEnd").getString(rideEnd);
            }
        } catch (Exception e) {
            PokeBus.getLogger().error("Could not load config.");
        }
    }

    public static void save() {
        try {
            node.getNode("Prefix").setValue(prefix);
            node.getNode("MaxDistance").setValue(maxDistance);
            node.getNode("MakeDriverInvisible").setValue(makeDriverInvisible);
            node.getNode("DriverNames").setValue(driverNames);
            node.getNode("RideStart").setValue(rideStart);
            node.getNode("RideEnd").setValue(rideEnd);
            loader.save(node);
        } catch (Exception e) {
            PokeBus.getLogger().error("Could not save config.");
        }
    }

    /*
    public static final String PATH = "config/pokebus/pokebus.json";
    public static PokeBusConfig INSTANCE;

    public String prefix = "&b[PokeBus] &r";
    public boolean useCustomNames;
    public ArrayList<String> driverNames = new ArrayList<>();

    public static void load() {
        INSTANCE = new PokeBusConfig();

        File file = new File(PATH);
        if (!file.exists())
            INSTANCE.save();
        else {
            try {
                FileReader fr = new FileReader(file);
                INSTANCE = new GsonBuilder().setPrettyPrinting().create().fromJson(fr, PokeBusConfig.class);

                // Save for when new options have been added
                INSTANCE.save();
                fr.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            File file = new File(PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            PrintWriter pw = new PrintWriter(file);
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
            pw.print(json);
            pw.flush();
            pw.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }*/
}
