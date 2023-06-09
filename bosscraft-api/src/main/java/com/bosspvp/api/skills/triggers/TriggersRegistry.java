package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public interface TriggersRegistry{

    /**
     * Get a trigger by [id].
     * <p>
     * This will enable the trigger.
     *
     * @param id the id
     * @return the trigger
     */
    @Nullable
    Trigger get(@NotNull String id);

    /**
     * Register a new [triggerGroup].
     *
     * @param triggerGroup the trigger group
     */
    void register(@NotNull TriggerGroup triggerGroup);

    /**
     * Get a predicate requiring certain trigger parameters.
     *
     * @param parameters the parameters
     * @return predicate
     */
    Predicate<Trigger> withParameters(@NotNull Set<TriggerParameter> parameters);


    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<Trigger> getRegistry();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
