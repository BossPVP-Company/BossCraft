package com.bosspvp.core.schedule;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.schedule.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class BossScheduler implements Scheduler {
    private final BossPlugin plugin;
    public BossScheduler(BossPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public BukkitTask runLater(long delay, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    @Override
    public BukkitTask runTimer(long delay, long repeat, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, repeat);
    }

    @Override
    public BukkitTask runTimerAsync(long delay, long repeat, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, repeat);
    }

    @Override
    public BukkitTask run(@NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public BukkitTask runAsync(@NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    @Override
    public BossPlugin getPlugin() {
        return plugin;
    }
}
