package com.bosspvp.core.events;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.events.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BossEventManager implements EventManager{

    private final Set<Listener> registry = new HashSet<>();

    private final BossPlugin plugin;
    public BossEventManager(BossPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public void registerListener(@NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        registry.add(listener);
    }

    @Override
    public Set<Listener> getRegisteredListeners() {
        return registry;
    }

    @Override
    public void unregisterListener(@NotNull Listener listener) {
        HandlerList.unregisterAll(listener);
        registry.remove(listener);
    }

    @Override
    public void unregisterAllListeners() {
        HandlerList.unregisterAll(plugin);
        registry.clear();
    }

    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }
}
