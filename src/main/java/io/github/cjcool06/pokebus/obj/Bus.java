package io.github.cjcool06.pokebus.obj;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.utils.Utils;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;

public class Bus {
    private final EntityPixelmon pokemon;
    private final BlockPos startLocation;
    private ArrayList<BlockPos> destinations;

    public Bus(EntityStatue statue, BlockPos startLocation) {
        this(Utils.getFromStatue(statue), startLocation);
    }

    public Bus(EntityPixelmon pokemon, BlockPos startLocation) {
        this.pokemon = pokemon;
        this.startLocation = startLocation;
        enrolInBusTraining();
    }

    private void enrolInBusTraining() {
        if (PokeBusConfig.INSTANCE.useCustomNames) {
            this.pokemon.setCustomNameTag("PokeBus Driver: " + Utils.getRandomNameFromConfig());
        }
        else {
            this.pokemon.setCustomNameTag("PokeBus Driver: " + pokemon.getPokemonName());
        }
        this.pokemon.getEntityData().setBoolean("IsBus", true);
        this.pokemon.setAIMoveSpeed(5);
    }

    public void addPassenger(Player player) {

    }

    public void removePassengers() {

    }

    public void retire() {

    }

    public EntityPixelmon getPokemon() {
        return pokemon;
    }

    public BlockPos getStartLocation() {
        return startLocation;
    }

    public ArrayList<BlockPos> getDestinations() {
        return destinations;
    }
}
