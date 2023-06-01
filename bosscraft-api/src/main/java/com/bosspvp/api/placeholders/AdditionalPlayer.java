package com.bosspvp.api.placeholders;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdditionalPlayer {
    /**
     * The player.
     */
    private final Player player;

    /**
     * The identifier.
     */
    private final String identifier;

    /**
     * Create a new additional player.
     *
     * @param player     The player.
     * @param identifier The identifier.
     */
    public AdditionalPlayer(@NotNull final Player player,
                            @NotNull final String identifier) {
        this.player = player;
        this.identifier = identifier;
    }

    /**
     * Get the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the identifier.
     *
     * @return The identifier.
     */
    public String getIdentifier() {
        return identifier;
    }
}
