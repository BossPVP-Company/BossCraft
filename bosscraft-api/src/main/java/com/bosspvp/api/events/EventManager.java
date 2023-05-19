package com.bosspvp.api.events;

import com.bosspvp.api.BossPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * EventManager
 * <p></p>
 * Manages BossPlugin event listeners
 */
public interface EventManager {

    /**
     * Register event listener
     *
     * @param listener The listener
     */
    void registerListener(@NotNull Listener listener);

    /**
     * Unregister event listener
     *
     * @param listener The listener
     */
    void unregisterListener(@NotNull Listener listener);

    /**
     * Unregister all event listeners
     *
     */
    void unregisterAllListeners();

    /**
     * Get the set of registered event listeners
     *
     * @return the listeners set
     */
    Set<Listener> getRegisteredListeners();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
