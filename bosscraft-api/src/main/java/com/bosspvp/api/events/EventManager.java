package com.bosspvp.api.events;

import com.bosspvp.api.BossPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface EventManager {
    void registerListener(@NotNull Listener listener);
    Set<Listener> getRegisteredListeners();

    void unregisterListener(@NotNull Listener listener);

    void unregisterAllListeners();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
