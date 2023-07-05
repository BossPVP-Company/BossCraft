package com.bosspvp.api.skills.visualeffects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//@TODO change this mess

public interface VisualEffectsManager {

    /**
     * Get a visual effect builder by [id].
     *
     * @param id the id
     * @return the trigger
     */
    @Nullable
    VisualEffectBuilder getEffectBuilder(@NotNull String id);

    /**
     * Register a new [visual effect builder].
     *
     * @param effectBuilder the visual effect
     */
    void registerEffectBuilder(@NotNull VisualEffectBuilder effectBuilder);


    /**
     * Start an effect
     *
     * @param effect The effect
     * @return The effect task id
     */
    VisualEffectTask startEffect(@NotNull VisualEffect effect);

    /**
     * Finish an effect
     *
     * @param effect The effect
     */
    void finishEffect(@NotNull VisualEffect effect);

    /**
     * cancel effect task with specified id.
     *
     * @param id An effect task id
     */
    void cancelEffectTask(@NotNull String id);

    /**
     * cancel all effects attached to this manager
     *
     */
    void cancelAllEffectTasks();

    /**
     *
     * @param id the effect id
     * @return list of effects
     */
    @NotNull
    VisualEffectTask getEffectTask(@NotNull String id);



    /**
     * Get the registry.
     *
     * @return The registry.
     */
    Registry<VisualEffect> getRegistry();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
