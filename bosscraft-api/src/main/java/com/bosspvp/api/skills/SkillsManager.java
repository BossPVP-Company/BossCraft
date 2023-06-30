package com.bosspvp.api.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.conditions.ConditionsRegistry;
import com.bosspvp.api.skills.counters.CountersRegistry;
import com.bosspvp.api.skills.effects.EffectsRegistry;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentsRegistry;
import com.bosspvp.api.skills.effects.executors.ChainExecutorRegistry;
import com.bosspvp.api.skills.filters.FilterRegistry;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.mutators.MutatorRegistry;
import com.bosspvp.api.skills.triggers.DispatchedTriggerFactory;
import com.bosspvp.api.skills.triggers.TriggersRegistry;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholdersRegistry;
import com.bosspvp.api.skills.visualeffects.VisualEffectsRegistry;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SkillsManager {

    /**
     * Initialize the manager.
     */
    void init();

    /**
     * Get the triggers registry.
     *
     * @return The triggers registry.
     */
    @NotNull TriggersRegistry getTriggersRegistry();

    /**
     * Get the trigger placeholders registry.
     *
     * @return The trigger placeholders registry.
     */
    @NotNull TriggerPlaceholdersRegistry getTriggerPlaceholdersRegistry();

    /**
     * Get the condition registry.
     *
     * @return The condition registry.
     */
    @NotNull ConditionsRegistry getConditionsRegistry();

    /**
     * Get the effect registry.
     *
     * @return The effect registry.
     */
    @NotNull EffectsRegistry getEffectsRegistry();

    /**
     * Get the effect arguments registry.
     *
     * @return The effect arguments registry.
     */
    @NotNull EffectArgumentsRegistry getEffectArgumentsRegistry();

    /**
     * Get the chain executor registry.
     *
     * @return The chain executor registry.
     */
    @NotNull ChainExecutorRegistry getChainExecutorsRegistry();

    /**
     * Get the filter registry.
     *
     * @return The filter registry.
     */
    @NotNull FilterRegistry getFilterRegistry();
    /**
     * Get the mutator registry.
     *
     * @return The mutator registry.
     */
    @NotNull MutatorRegistry getMutatorRegistry();
    /**
     * Get the holder manager.
     *
     * @return The holder manager.
     */
    @NotNull HolderManager getHolderManager();

    /**
     * Get the counter registry.
     *
     * @return The counter registry.
     */
    @NotNull CountersRegistry getCountersRegistry();

    /**
     * Get the dispatched trigger factory.
     *
     * @return The dispatched trigger factory.
     */
    @NotNull DispatchedTriggerFactory getDispatchedTriggerFactory();


    /**
     * Get the visual effect registry.
     *
     * @return The visual effect registry.
     */
    @NotNull VisualEffectsRegistry getVisualEffectsRegistry();
    /**
     * Load the listeners.
     *
     * @return The listeners.
     */
    @NotNull List<Listener> loadListeners();

    /**
     * Start the manager's tasks
     */
    void startTasks();


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();

}
