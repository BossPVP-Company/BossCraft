package com.bosspvp.api;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.config.ConfigManager;
import com.bosspvp.api.events.EventManager;
import com.bosspvp.api.gui.GuiController;
import com.bosspvp.api.gui.menu.MenuBuilder;
import com.bosspvp.api.gui.slot.SlotBuilder;
import com.bosspvp.api.misc.drops.DropQueue;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.schedule.Scheduler;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.triggers.DispatchedTriggerFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Bridge between api and core.
 *
 * @see BossAPI#getInstance()
 * @see BossPlugin#getAPI()
 */
public interface BossAPI {

    /**
     * Create a scheduler.
     *
     * @param plugin The plugin.
     * @return The scheduler.
     */
    @NotNull
    Scheduler createScheduler(@NotNull BossPlugin plugin);

    /**
     * Create an event manager.
     *
     * @param plugin The plugin.
     * @return The event manager.
     */
    @NotNull
    EventManager createEventManager(@NotNull BossPlugin plugin);

    /**
     * Create a config manager
     *
     * @param plugin The plugin.
     * @return The manager
     */
    @NotNull
    ConfigManager createConfigManager(@NotNull BossPlugin plugin);

    /**
     * Create plugin logger.
     *
     * @param plugin The plugin.
     * @return The logger.
     */
    @NotNull
    Logger createLogger(@NotNull BossPlugin plugin);

    /**
     * Create drop queue
     *
     * @param player The player
     * @return The drop queue.
     */
    @NotNull
    DropQueue createDropQueue(Player player);

    /**
     * Create slot builder
     *
     * @param positions The slot positions
     * @return The slot builder.
     */
    @NotNull
    SlotBuilder createSlotBuilder(int ... positions);

    /**
     * Create menu builder
     *
     * @param plugin The plugin.
     * @return The menu builder.
     */
    @NotNull
    MenuBuilder createMenuBuilder(@NotNull BossPlugin plugin,
                                  int rows);

    /**
     * Create gui controller
     *
     * @param plugin The plugin.
     * @return The gui controller.
     */
    @NotNull
    GuiController createGuiController(@NotNull BossPlugin plugin);

    /**
     * Creates delegated config class
     *
     * @param ymlHandle The Yaml file
     * @param handle    The section to delegate
     * @return The delegated config
     */
    @NotNull
    Config createDelegatedConfig(@NotNull YamlConfiguration ymlHandle,
                                 @NotNull ConfigurationSection handle);



    /**
     * Create Skills Manager
     *
     * @param plugin The plugin
     * @return The skills manager
     */
    @NotNull
    SkillsManager createSkillsManager(@NotNull BossPlugin plugin);

    /**
     * Evaluate an expression.
     *
     * @param expression The expression.
     * @param context    The context.
     * @return The value of the expression, or zero if invalid.
     */
    double evaluate(@NotNull String expression,
                    @NotNull PlaceholderContext context);

    /**
     * Get saved plugin by name
     *
     */
    @Nullable
    BossPlugin getPluginByName(@NotNull String name);

    /**
     * Add boss plugin to the list
     *
     */
    void addPlugin(@NotNull BossPlugin plugin);

    /**
     * Get the plugin which loads api implementation
     *
     */
    @NotNull
    BossPlugin getCorePlugin();


    /**
     * Get BossAPI instance
     *
     * @return BossAPI instance
     */
    static BossAPI getInstance() {
        return Instance.get();
    }
    final class Instance {
        private static BossAPI api;
        private Instance() {
            throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
        }

        static void set(@NotNull final BossAPI api) {
            if(Instance.api != null) return;

            Instance.api = api;
        }


        static BossAPI get() {
            return api;
        }

    }
}
