package com.bosspvp.api.misc.sound;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.config.serialization.ConfigDeserializer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A sound that can be played at a location.
 *
 * @param sound  The sound.
 * @param pitch  The pitch.
 * @param volume The volume.
 */
public record PlayableSound(@NotNull Sound sound,
                            double pitch,
                            double volume) {
    /**
     * The deserializer.
     */
    private static final ConfigDeserializer<PlayableSound> DESERIALIZER = new Deserializer();

    /**
     * Create a sound with a default volume.
     *
     * @param sound The sound.
     * @param pitch The pitch.
     */
    public PlayableSound(@NotNull final Sound sound,
                         final double pitch) {
        this(sound, pitch, 1.0);
    }

    /**
     * Play the sound to a player.
     *
     * @param player The player.
     */
    public void playTo(@NotNull final Player player) {
        player.playSound(player.getLocation(), sound, (float) volume, (float) pitch);
    }

    /**
     * Play the sound at a location.
     *
     * @param location The location.
     */
    public void playAt(@NotNull final Location location) {
        World world = location.getWorld();

        if (world == null) {
            return;
        }

        world.playSound(location, sound, (float) volume, (float) pitch);
    }

    /**
     * Parse a playable sound from config.
     *
     * @param config The config.
     * @return The sound, or null if it's invalid.
     */
    @Nullable
    public static PlayableSound create(@NotNull final Config config) {
        return DESERIALIZER.deserializeFromConfig(config);
    }

    /**
     * The deserializer for {@link PlayableSound}.
     */
    private static final class Deserializer implements ConfigDeserializer<PlayableSound> {
        @Override
        public @Nullable PlayableSound deserializeFromConfig(@NotNull final Config config) {
            if (!config.hasPath("sound")) {
                return null;
            }

            try {
                Sound sound = Sound.valueOf(config.getString("sound").toUpperCase());

                double pitch = config.getDoubleOrDefault("pitch",1.0);
                double volume = config.getDoubleOrDefault("volume",1.0);

                return new PlayableSound(
                        sound,
                        pitch,
                        volume
                );
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}