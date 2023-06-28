package com.bosspvp.api.misc.ecomomy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


/**
 * A price that a player should pay.
 */
public interface Price {
    /**
     * Get if a player can afford to pay the price.
     *
     * @param player The player.
     * @return If the player can afford.
     */
    default boolean canAfford(@NotNull final Player player) {
        return this.canAfford(player, 1);
    }

    /**
     * Get if a player can afford to pay x times the price.
     *
     * @param player     The player.
     * @param multiplier The multiplier.
     * @return If the player can afford.
     */
    boolean canAfford(@NotNull final Player player,
                              final double multiplier);
    /**
     * Make the player pay the price.
     * <p>
     * Check canAfford first.
     *
     * @param player The player.
     */
    default void pay(@NotNull final Player player) {
        this.pay(player, 1);
    }

    /**
     * Make the player pay the price x times.
     * <p>
     * Check canAfford first.
     *
     * @param player     The player.
     * @param multiplier The multiplier.
     */
    void pay(@NotNull final Player player,
                     final double multiplier);

    /**
     * Give the price to the player.
     *
     * @param player The player.
     */
    default void giveTo(@NotNull final Player player) {
        this.giveTo(player, 1);
    }

    /**
     * Give the price to the player x times.
     *
     * @param player     The player.
     * @param multiplier The multiplier.
     */
    void giveTo(@NotNull final Player player,
                final double multiplier);

    /**
     * Get the numerical value that backs this price.
     * <p>
     * Should always return the value with player multipliers applied.
     *
     * @param player The player.
     * @return The value.
     */
    default double getValue(@NotNull final Player player) {
        return getValue(player, getMultiplier(player));
    }

    /**
     * Get the numeral value that backs this price multiplied x times.
     *
     * @param player     The player.
     * @param multiplier The multiplier.
     * @return The value.
     */
    double getValue(@NotNull final Player player,
                    final double multiplier);

    /**
     * Get the value multiplier for the player.
     *
     * @param player The player.
     * @return The multiplier.
     */
    double getMultiplier(@NotNull final Player player);

    /**
     * Set the value multiplier for the player.
     *
     * @param player     The player.
     * @param multiplier The multiplier.
     */
    void setMultiplier(@NotNull final Player player,
                       final double multiplier);

    /**
     * Get the identifier of this price (as type/instance checks break with delegation,
     * this is used for combining prices, etc.)
     * <p>
     * By default, this uses the class name, but it's good practice to override this.
     * <p>
     * It's also good practice to prefix your identifiers with some kind of namespace or
     * internal ID, in order to prevent conflicts.
     *
     * @return The identifier.
     */
    default String getIdentifier() {
        return this.getClass().getName();
    }

}
