package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ConditionsRegistry {
    

    /**
     * Compile a list of [configs] into a ConditionList in a given [context].
     *
     * @param configs The configs to compile.
     * @param context The context to compile in.
     * @return The compiled ConditionList.
     */
    @NotNull
    ConditionList compile(@NotNull List<Config> configs,
                          @NotNull ViolationContext context);
    /**
     * Compile a [cfg] into a ConditionBlock in a given [context].
     *
     * @param config The config to compile.
     * @param context The context to compile in.
     * @return The compiled ConditionBlock.
     */
    @Nullable ConditionBlock<?> compile(@NotNull Config config,
                                        @NotNull ViolationContext context);


    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<Condition<?>> getRegistry();
    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();

}
