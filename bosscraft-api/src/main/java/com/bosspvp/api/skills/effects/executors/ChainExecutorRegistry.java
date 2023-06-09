package com.bosspvp.api.skills.effects.executors;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.triggers.Trigger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ChainExecutorRegistry {

    /**
     * Get executor by ID, or null if invalid ID is provided.
     * Returns normal if the ID is null.
     *
     * @param id the id
     * @return the chain executor
     */
    @NotNull
    ChainExecutor getOrNormal(@Nullable String id);

    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<ChainExecutorFactory> getRegistry();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
