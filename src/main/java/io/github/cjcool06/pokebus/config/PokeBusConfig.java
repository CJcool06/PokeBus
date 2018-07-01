package io.github.cjcool06.pokebus.config;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PokeBusConfig {
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
    }
}
