package com.bosspvp.api.skills.counters;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CountersRegistry {
    /**
     * Compile a counter from a [cfg] in a [context].
     */
    @Nullable Counter compile(@NotNull Config config, @NotNull ViolationContext context);

    /**
     * Bind a [accumulator] to an [counter].
     */
    void bindCounter(@NotNull Counter counter, @NotNull Accumulator accumulator);

    /**
     * Unbind a [counter].
     */
    void unbindCounter(@NotNull Counter counter);

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
