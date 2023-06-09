package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface EffectsRegistry {

    /**
     * Get a chain by its [id].
     *
     * @param id the id
     * @return the chain
     */
    @Nullable
    Chain getChainByID(@NotNull String id);

    /**
     * Register a new [chain] with a certain [id].
     *
     * @param id the id
     * @param chain the chain
     */
    void register(@NotNull String id, @NotNull Chain chain);

    /**
     * Compile a list of [configs] into an EffectList in a given [context].
     *
     * @param context the context
     * @param configs the configs
     * @return the effect block
     */
    @Nullable
    EffectList compile(@NotNull Collection<Config> configs,
                       @NotNull ViolationContext context);

    /**
     * Compile a [config] into an EffectBlock in a given [context].
     *
     * @param config the config
     * @param context the context
     * @return the effect block
     */
    @Nullable
    EffectBlock compile(@NotNull Config config,
                        @NotNull ViolationContext context);

    /**
     * Compile a list of [configs] into a Chain with a given [context].
     *
     * @param configs the configs
     * @param executor the executor
     * @param context the context
     * @return the chain
     */
    @Nullable
    Chain compileChain(@NotNull Collection<Config> configs,
                       @NotNull ChainExecutor executor,
                       @NotNull ViolationContext context);


    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<Effect<?>> getRegistry();
    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();



}
