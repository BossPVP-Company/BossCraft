package com.bosspvp.api.misc.drops;

import com.bosspvp.api.BossAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DropQueue {

    /**
     * Create a new DropQueue.
     *
     * @param player The player.
     */
    static DropQueue create(@NotNull final Player player) {
        return BossAPI.getInstance().createDropQueue(player);
    }

    /**
     * Add item to queue.
     *
     * @param item The item to add.
     * @return The DropQueue.
     */
    DropQueue addItem(@NotNull final ItemStack item);

    /**
     * Add multiple items to queue.
     *
     * @param itemStacks The items to add.
     * @return The DropQueue.
     */
    DropQueue addItems(@NotNull final Collection<ItemStack> itemStacks);

    /**
     * Add xp to queue.
     *
     * @param amount The amount to add.
     * @return The DropQueue.
     */
    DropQueue addExperience(final int amount);

    /**
     * Set location of the origin of the drops.
     *
     * @param location The location.
     * @return The DropQueue.
     */
    DropQueue setLocation(@NotNull final Location location);

    /**
     * Force the queue to act as if player is telekinetic.
     *
     * @return The DropQueue.
     */
    DropQueue forceTelekinesis();

    /**
     * Push the queue.
     */
    void push();
}
