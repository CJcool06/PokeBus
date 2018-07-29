package io.github.cjcool06.pokebus.obj;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.github.cjcool06.pokebus.ai.BusAI;
import io.github.cjcool06.pokebus.config.PokeBusConfig;
import io.github.cjcool06.pokebus.utils.Utils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;

public class Bus {
    private final BusStop busStop;
    private final Player passenger;
    private final EntityPixelmon pokemon;
    private final ArrayList<Destination> destinationsPending;
    private final Entity driver;
    private final String name;

    public Bus(BusStop busStop, Player passenger) {
        this.busStop = busStop;
        this.passenger = passenger;
        this.pokemon = Utils.getFromStatue(busStop.getStatue());
        this.destinationsPending = new ArrayList<>(busStop.getDestinations());
        this.driver = passenger.getWorld().createEntity(EntityTypes.CHICKEN, passenger.getPosition());
        this.name = busStop.getDriverNames().isEmpty() ? Utils.getRandomNameFromConfig() : Utils.getRandomName(busStop.getDriverNames());
        enrolInBusTraining();
        busStop.getBuses().add(this);
    }

    private void enrolInBusTraining() {
        EntityPlayerMP playerMP = (EntityPlayerMP)passenger;
        this.pokemon.setLocationAndAngles(playerMP.getPosition().getX(), playerMP.getPosition().getY(), playerMP.getPosition().getZ(), playerMP.rotationYaw, playerMP.rotationPitch);
        this.pokemon.getEntityData().setBoolean("IsBus", true);
        ((EntityLiving)this.driver).getEntityData().setBoolean("IsBusDriver", true);
        this.pokemon.tasks.taskEntries.clear();
        this.pokemon.targetTasks.taskEntries.clear();
        ((Entity)this.pokemon).offer(Keys.INVULNERABLE, true);
        playerMP.getEntityWorld().spawnEntity(pokemon);

        ((EntityLiving)this.driver).tasks.taskEntries.clear();
        ((EntityLiving)this.driver).targetTasks.taskEntries.clear();
        ((EntityLiving)this.driver).tasks.addTask(1, new BusAI(this, busStop.getTravelSpeed()));
        this.driver.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize(name));
        this.driver.offer(Keys.INVULNERABLE, true);
        if (PokeBusConfig.makeDriverInvisible) {
            List<PotionEffect> effects = new ArrayList<>();
            PotionEffect effect = PotionEffect.builder().potionType(PotionEffectTypes.INVISIBILITY).particles(false).duration(Integer.MAX_VALUE).build();
            effects.add(effect);
            this.driver.offer(Keys.POTION_EFFECTS, effects);
        }
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
            passenger.getWorld().spawnEntity(this.driver);
        }

        this.driver.addPassenger(passenger);
        ((Entity)pokemon).addPassenger(this.driver);
    }

    public void removePassenger() {
        driver.clearPassengers();
    }

    public void retire() {
        removePassenger();
        if (!this.driver.isRemoved()) {
            this.driver.remove();
        }
        if (this.pokemon.isEntityAlive()) {
            this.pokemon.getEntityWorld().removeEntity(this.pokemon);
        }
        destinationsPending.clear();
        busStop.getBuses().remove(this);
    }

    public String getName() {
        return name;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public Player getPassenger() {
        return passenger;
    }

    public EntityPixelmon getPokemon() {
        return pokemon;
    }

    public Entity getDriver() {
        return driver;
    }

    public ArrayList<Destination> getDestinationsPending() {
        return destinationsPending;
    }

    public void sendMessage(Text text) {
        passenger.sendMessage(Text.of(TextColors.DARK_AQUA, "[", TextColors.LIGHT_PURPLE, TextSerializers.FORMATTING_CODE.deserialize(name), TextColors.DARK_AQUA, "]", TextColors.GRAY, " - ", text));
    }
}
