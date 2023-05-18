package com.bosspvp.api;

import com.bosspvp.api.events.EventManager;
import com.bosspvp.api.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

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
     * Create plugin logger.
     *
     * @param plugin The plugin.
     * @return The logger.
     */
    @NotNull
    Logger createLogger(@NotNull BossPlugin plugin);







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
