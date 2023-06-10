package com.bosspvp.api.skills.filters;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;

public interface FilterRegistry {

    /**
     * Compile a [config] into a FilterList a given [context].
     *
     * @param config The config to compile
     * @param context The context to compile the config with
     * @return The compiled FilterList
     */
    @NotNull
    FilterList compile(@NotNull Config config,@NotNull ViolationContext context);

    /**
     * Get the registry.
     *
     * @return The registry.
     */
    @NotNull
    Registry<Filter<?,?>> getRegistry();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
