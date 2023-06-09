package com.bosspvp.api.skills.triggers.placeholders;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface TriggerPlaceholdersRegistry {

    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<TriggerPlaceholder> getRegistry();
    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
