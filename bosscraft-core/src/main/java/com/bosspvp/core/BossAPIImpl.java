package com.bosspvp.core;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.ConfigManager;
import com.bosspvp.api.events.EventManager;
import com.bosspvp.api.gui.GuiController;
import com.bosspvp.api.gui.menu.MenuBuilder;
import com.bosspvp.api.gui.slot.SlotBuilder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.schedule.Scheduler;
import com.bosspvp.core.config.BossConfigManager;
import com.bosspvp.core.events.BossEventManager;
import com.bosspvp.core.gui.BossGuiController;
import com.bosspvp.core.gui.BossMenuBuilder;
import com.bosspvp.core.gui.BossSlotBuilder;
import com.bosspvp.core.logger.BossLogger;
import com.bosspvp.core.math.Evaluator;
import com.bosspvp.core.schedule.BossScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.logging.Logger;

public class BossAPIImpl implements BossAPI {
    private HashMap<String, BossPlugin> loadedPlugins = new HashMap<>();
    private Evaluator evaluator = new Evaluator();
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

    @Override
    public @NotNull SlotBuilder createSlotBuilder(int ... positions) {
        if(positions.length==0)
            throw new IllegalArgumentException("positions argument for GUI SLOT cannot be empty!");

        return new BossSlotBuilder(positions);
    }

    @Override
    public @NotNull MenuBuilder createMenuBuilder(@NotNull BossPlugin plugin, int rows) {
        return new BossMenuBuilder(plugin,rows);
    }

    @Override
    public @NotNull GuiController createGuiController(@NotNull BossPlugin plugin) {
        return new BossGuiController(plugin);
    }

    @Override
    public double evaluate(@NotNull String expression, @NotNull PlaceholderContext context) {
        return evaluator.evaluate(expression,context);
    }


    @Override
    public @Nullable BossPlugin getPluginByName(@NotNull String name) {
        return loadedPlugins.get(name.toLowerCase());
    }
}
