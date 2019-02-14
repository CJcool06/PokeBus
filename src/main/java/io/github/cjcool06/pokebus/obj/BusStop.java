package io.github.cjcool06.pokebus.obj;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import io.github.cjcool06.pokebus.commands.StopsCommand;
import io.github.cjcool06.pokebus.listeners.ChatListener;
import io.github.cjcool06.pokebus.managers.BusManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.UUID;

public class BusStop {
    private final UUID statueUID;
    private final World world;
    private final String name;
    private final ArrayList<Destination> destinations = new ArrayList<>();
    private final ArrayList<Bus> buses = new ArrayList<>();
    private final ArrayList<String> driverNames;
    private double travelSpeed = 1;
    public boolean isActive = true;

    public BusStop(EntityStatue statue, String name) {
        this(statue.getUniqueID(), (World)statue.getEntityWorld(), name, new ArrayList<>());
    }

    public BusStop(UUID statueUID, World world, String name, ArrayList<String> driverNames) {
        this.statueUID = statueUID;
        this.world = world;
        this.name = name;
        this.driverNames = driverNames;
        BusManager.registerBusStop(this);
    }

    public EntityStatue getStatue() {
        if (world.getEntity(statueUID).isPresent()) {
            return (EntityStatue)world.getEntity(statueUID).get();
        }
        return null;
    }

    public UUID getStatueUID() {
        return statueUID;
    }

    public World getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getDriverNames() {
        return driverNames;
    }

    public double getTravelSpeed() {
        return travelSpeed;
    }

    public void setTravelSpeed(double travelSpeed) {
        this.travelSpeed = travelSpeed;
    }

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public void sendPlayer(Player player) {
        BlockPos blockPos = getStatue().getPosition();
        ((EntityPlayerMP)player).setPositionAndUpdate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public void editDestinations(CommandSource src) {
        ArrayList<Text> contents = new ArrayList<>();

        contents.add(Text.builder().append(Text.of(TextColors.GOLD, "[<--]"))
                .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to go back")))
                .onClick(TextActions.executeCallback(dummySrc -> {
                    StopsCommand.showStopsSummary(src);
                }))
                .build());

        for (Destination destination : destinations) {
            contents.add(Text.builder()
                    .append(Text.builder().append(Text.of(TextColors.RED, "[", TextColors.DARK_RED, "-", TextColors.RED, "]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to delete this destination")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                this.destinations.remove(destination);
                                src.sendMessage(Text.of(TextColors.RED, "Deleted destination ", TextColors.LIGHT_PURPLE, destination.getName(), TextColors.RED,
                                        " from PokeBus Stop ", TextColors.AQUA, getName(), TextColors.RED, "."));
                                editDestinations(src);
                            }))
                            .build())
                    .append(Text.builder().append(Text.of(TextColors.DARK_AQUA, " [", TextColors.AQUA, destination.getName(), TextColors.DARK_AQUA, "]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to teleport to this destination")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                destination.sendPlayer((Player)src);
                                src.sendMessage(Text.of(TextColors.GREEN, "Teleported to destination ", TextColors.LIGHT_PURPLE, destination.getName(), TextColors.GREEN,
                                        " of PokeBus Stop ", TextColors.AQUA, getName(), TextColors.GREEN, "."));
                            }))
                            .build())
                    .append(Text.of(TextColors.GRAY, " - "))
                    .append(Text.builder().append(Text.of(destination.isGhost ? TextColors.GREEN : TextColors.RED, "[Ghost]"))
                            .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to toggle this destination's ghosting")))
                            .onClick(TextActions.executeCallback(dummySrc -> {
                                destination.isGhost = !destination.isGhost;
                                editDestinations(src);
                            }))
                            .build())
                    .build()
            );
        }

        contents.add(Text.of(
                TextActions.executeCallback(dummySrc -> {
                    ChatListener.clearListeners((Player)src);
                    ChatListener.listeningForChatBusStops.put((Player)src, this);
                    src.sendMessage(Text.of(TextColors.GRAY, "What is the name of this destination?"));
                }),
                TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to create a new destination")),
                TextColors.GREEN, "[+]"
        ));

        PaginationList.builder()
                .title(Text.of(TextColors.LIGHT_PURPLE, " Destinations "))
                .contents(contents)
                .padding(Text.of(TextColors.GRAY, "-", TextColors.RESET))
                .sendTo(src);
    }

    public void editOther(CommandSource src) {
        ArrayList<Text> contents = new ArrayList<>();

        contents.add(Text.builder().append(Text.of(TextColors.GOLD, "[<--]"))
                .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to go back")))
                .onClick(TextActions.executeCallback(dummySrc -> {
                    StopsCommand.showStopsSummary(src);
                }))
                .build());

        if (isActive) {
            contents.add(Text.builder().append(Text.of(TextColors.GREEN, "[Active]"))
                    .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to make this PokeBus Stop inactive")))
                    .onClick(TextActions.executeCallback(dummySrc -> {
                        isActive = false;
                        editOther(src);
                    }))
                    .build());
        }
        else {
            contents.add(Text.builder().append(Text.of(TextColors.RED, "[Inactive]"))
                    .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to make this PokeBus Stop active")))
                    .onClick(TextActions.executeCallback(dummySrc -> {
                        isActive = true;
                        editOther(src);
                    }))
                    .build());
        }

        Text.Builder builder = Text.builder();
        builder.append(Text.builder().append(
                Text.builder().append(Text.of(travelSpeed == 0.5 ? TextColors.GREEN : TextColors.RED, "[", travelSpeed == 0.5 ? TextColors.DARK_GREEN : TextColors.DARK_RED, 0.5, travelSpeed == 0.5 ? TextColors.GREEN : TextColors.RED, "]"))
                        .onClick(TextActions.executeCallback(dummySrc -> {
                            if (travelSpeed != 0.5) {
                                travelSpeed = 0.5;
                                editOther(src);
                            }
                        })).build())
                .build());
        builder.append(Text.builder().append(
                Text.builder().append(Text.of(travelSpeed == 0.75 ? TextColors.GREEN : TextColors.RED, "[", travelSpeed == 0.75 ? TextColors.DARK_GREEN : TextColors.DARK_RED, 0.75, travelSpeed == 0.75 ? TextColors.GREEN : TextColors.RED, "]"))
                        .onClick(TextActions.executeCallback(dummySrc -> {
                            if (travelSpeed != 0.75) {
                                travelSpeed = 0.75;
                                editOther(src);
                            }
                        })).build())
                .build());
        for (int i = 1; i <= 10; i++) {
            // needed for executeCallback lambda
            int speed = i;
            if (travelSpeed == speed) {
                builder.append(Text.builder().append(Text.of(TextColors.GREEN, "[", TextColors.DARK_GREEN, speed, TextColors.GREEN, "]")).build()).build();
            }
            else {
                builder.append(
                        Text.builder().append(Text.of(TextColors.RED, "[", TextColors.DARK_RED, speed, TextColors.RED, "]"))
                                .onClick(TextActions.executeCallback(dummySrc -> {
                                    travelSpeed = speed;
                                    editOther(src);
                                })).build())
                        .build();
            }
        }

        contents.add(Text.builder().append(Text.builder()
                .append(Text.of(TextColors.AQUA, "[Speed]")).build())
                .append(Text.of(TextColors.GRAY, " - "))
                .append(builder.build())
                .build());

        PaginationList.builder()
                .title(Text.of(TextColors.LIGHT_PURPLE, " Other "))
                .contents(contents)
                .padding(Text.of(TextColors.GRAY, "-", TextColors.RESET))
                .sendTo(src);
    }

    public void editNames(CommandSource src) {
        ArrayList<Text> contents = new ArrayList<>();

        contents.add(Text.builder().append(Text.of(TextColors.GOLD, "[<--]"))
                .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to go back")))
                .onClick(TextActions.executeCallback(dummySrc -> {
                    StopsCommand.showStopsSummary(src);
                }))
                .build());

        if (driverNames.isEmpty()) {
            contents.add(Text.builder()
                    .append(Text.of(TextColors.GOLD, "[Default]"))
                    .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Using default driver names from config"))).build());
        }
        else {
            for (String name : driverNames) {
                contents.add(Text.builder()
                        .append(Text.builder().append(Text.of(TextColors.RED, "[", TextColors.DARK_RED, "-", TextColors.RED, "]"))
                                .onHover(TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to delete this name")))
                                .onClick(TextActions.executeCallback(dummySrc -> {
                                    driverNames.remove(name);
                                    editNames(src);
                                }))
                                .build())
                        .append(Text.of(" ", TextColors.AQUA, TextSerializers.FORMATTING_CODE.deserialize(name))).build());
            }
        }

        contents.add(Text.of(
                TextActions.executeCallback(dummySrc -> {
                    ChatListener.clearListeners((Player)src);
                    ChatListener.listeningForName.put((Player)src, this);
                    src.sendMessage(Text.of(TextColors.GRAY, "Enter a name:"));
                }),
                TextActions.showText(Text.of(TextColors.GRAY, TextStyles.ITALIC, "Click to add a new name")),
                TextColors.GREEN, "[+]"
        ));

        PaginationList.builder()
                .title(Text.of(TextColors.LIGHT_PURPLE, " Driver Names "))
                .contents(contents)
                .padding(Text.of(TextColors.GRAY, "-", TextColors.RESET))
                .sendTo(src);
    }
}
