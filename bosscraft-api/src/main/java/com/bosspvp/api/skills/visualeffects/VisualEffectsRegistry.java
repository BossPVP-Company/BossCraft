package com.bosspvp.api.skills.visualeffects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//@TODO change this mess

public interface VisualEffectsRegistry {

    /**
     * Get a visual effect by [id].
     *
     * @param id the id
     * @return the trigger
     */
    @Nullable
    VisualEffect get(@NotNull String id);

    /**
     * Register a new [visual effect].
     *
     * @param triggerGroup the visual effect
     */
    void register(@NotNull VisualEffect triggerGroup);


    /**
     * Start an effect
     *
     * @param effect The effect
     * @return The effect task id
     */
    String startEffect(@NotNull VisualEffect effect);

    /**
     * Finish an effect
     *
     * @param effect The effect
     */
    void finishEffect(@NotNull VisualEffect effect);

    /**
     * cancel effects with specified id.
     *
     * @param id An effect task id
     */
    void cancelEffectsByTaskID(@NotNull String id);

    /**
     * cancel all effects attached to this manager
     *
     */
    void cancelAllEffects();

    /**
     * get all active effects with specified parent task id.
     * <p> In most cases returns a single element, but the effect which uses the repeat feature,
     * will return a list of active effects attached to the original task id
     *
     * @param id the effect id
     * @return list of effects
     */
    @NotNull
    List<VisualEffect> getEffectsByParentTaskID(@NotNull String id);



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
