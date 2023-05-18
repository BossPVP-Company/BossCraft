package com.bosspvp.core;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.ConfigManager;
import com.bosspvp.api.events.EventManager;
import com.bosspvp.api.schedule.Scheduler;
import com.bosspvp.core.config.BossConfigManager;
import com.bosspvp.core.events.BossEventManager;
import com.bosspvp.core.logger.BossLogger;
import com.bosspvp.core.schedule.BossScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class BossAPIImpl implements BossAPI {
    @Override
    public @NotNull Scheduler createScheduler(@NotNull BossPlugin plugin) {
        return new BossScheduler(plugin);
    }

    @Override
    public @NotNull EventManager createEventManager(@NotNull BossPlugin plugin) {
        return new BossEventManager(plugin);
    }

    @Override
    public @NotNull ConfigManager createConfigManager(@NotNull BossPlugin plugin) {
        return new BossConfigManager(plugin);
    }

    @Override
    public @NotNull Logger createLogger(@NotNull BossPlugin plugin) {
        return new BossLogger(plugin);
    }
}
