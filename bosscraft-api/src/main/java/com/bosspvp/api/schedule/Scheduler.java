package com.bosspvp.api.schedule;

import com.bosspvp.api.BossPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * Plugin Scheduler
 * <p></p>
 * Uses {@link org.bukkit.scheduler.BukkitScheduler}
 * Contains more convenient methods and attached to the BossPlugin
 */
public interface Scheduler {
    /**
     * Run the task after a specified tick delay.
     *
     * @param delay The amount of ticks to wait before execution.
     * @param runnable   The task to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runLater(long delay,
                        @NotNull Runnable runnable);


    /**
     * Run the task repeatedly on a timer.
     *
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @param runnable The task to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runTimer(long delay,
                        long repeat,
                        @NotNull Runnable runnable);


    /**
     * Run the task repeatedly and asynchronously on a timer.
     *
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @param runnable The task to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runTimerAsync(long delay,
                             long repeat,
                             @NotNull Runnable runnable);


    /**
     * Run the task once, at the next tick
     *
     * @param runnable The task to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask run(@NotNull Runnable runnable);

    /**
     * Run the task asynchronously.
     *
     * @param runnable The task to run.
     * @return The created {@link BukkitTask}.
     */
    BukkitTask runAsync(@NotNull Runnable runnable);


    /**
     * Cancel all running tasks from the linked {@link BossPlugin}.
     */
    void cancelAll();


    BossPlugin getPlugin();

}
