package com.bosspvp.api.skills.effects.arguments;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EffectArgumentsRegistry {

    /**
     * compile the config into a list of arguments
     * @param config the config
     * @param context the context
     * @return the list of arguments
     */
    @Nullable EffectArgumentList compile(@NotNull Config config, @NotNull ViolationContext context);

    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<EffectArgument<?>> getRegistry();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();

}
