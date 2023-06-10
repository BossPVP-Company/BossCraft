package com.bosspvp.api.skills.mutators;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface MutatorRegistry {

    /**
     * Compile a list of [configs] into a MutatorList in a given [context].
     *
     * @param configs The configs to compile.
     * @param context The context to compile the configs with.
     * @return The compiled MutatorList.
     */
    @NotNull
    MutatorList compile(@NotNull Collection<Config> configs,
                        @NotNull ViolationContext context);

    /**
     * Compile a [config] into a MutatorBlock in a given [context].
     *
     * @param config The config to compile.
     * @param context The context to compile the config with.
     * @return The compiled MutatorBlock.
     */
    @Nullable
    MutatorBlock<?> compile(@NotNull Config config,
                            @NotNull ViolationContext context);

    /**
     * Get the registry.
     *
     * @return The registry.
     */
    @NotNull
    Registry<Mutator<?>> getRegistry();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
