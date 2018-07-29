package io.github.cjcool06.pokebus.coremod;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("PokeBusCore")
@IFMLLoadingPlugin.DependsOn("pixelmon")
public class PokeBusCoreMod implements IFMLLoadingPlugin {
    private File modFile = null;
    //private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("PokeBusCore");

    public PokeBusCoreMod() {
        findAndLoadPixelmon();
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.pokebus.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {
        modFile = (File)map.get("coremodLocation");
        if (modFile == null) {
            modFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    // Credit to happyzlife for this bad boi
    private void findAndLoadPixelmon() {
        try {
            //logger.info("Trying to load pixelmon...");
            File modsFolder = new File(System.getProperty("user.dir"), "mods");
            if (!modsFolder.exists()) {
                //logger.warning("The mods folder couldn't be found.\nFolder: " + modsFolder.toString());
            }

            //Scanning through /mods/ searching for jars
            Collection<File> jars = FileUtils.listFiles(modsFolder, new String[]{"jar"}, false);
            File pixelmon = null;
            for (File jar : jars) {
                ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    zip.closeEntry();

                    //Checking if the jar is the pixelmon
                    if (entry.getName().equals("com/pixelmonmod/pixelmon/Pixelmon.class")) {
                        pixelmon = jar;
                        break;
                    }
                }
                zip.close();
                if (pixelmon != null) break;
            }

            //Opsie dopsie, no pixelmon jars found :(
            if (pixelmon == null) {
                //logger.warning("Pixelmon's jar cannot be found, the program will not continue.");
                return;
            }

            //Once we know the file that contains the pixelmon, it's better to check if another coremod already loaded it
            if (!CoreModManager.getReparseableCoremods().contains(pixelmon.getName())) {

                //Now we're safe to load the jar (thanks to clienthax)
                ((LaunchClassLoader) this.getClass().getClassLoader()).addURL(pixelmon.toURI().toURL());
                CoreModManager.getReparseableCoremods().add(pixelmon.getName());
            }
            //logger.info("Loaded!");
        } catch (Exception e) {
            //logger.warning("There was a problem trying to find Pixelmon's jar, the program will not continue.");
            e.printStackTrace();
            return;
        }
    }
}
