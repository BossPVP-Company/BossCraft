package com.bosspvp.api.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.conditions.ConditionsRegistry;
import com.bosspvp.api.skills.effects.EffectsRegistry;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentsRegistry;
import com.bosspvp.api.skills.effects.executors.ChainExecutorRegistry;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.holder.HolderUpdaterListener;
import com.bosspvp.api.skills.triggers.DispatchedTriggerFactory;
import com.bosspvp.api.skills.triggers.TriggersRegistry;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholdersRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SkillsManager {

    TriggersRegistry getTriggersRegistry();
    TriggerPlaceholdersRegistry getTriggerPlaceholdersRegistry();
    ConditionsRegistry getConditionsRegistry();

    EffectsRegistry getEffectsRegistry();
    EffectArgumentsRegistry getEffectArgumentsRegistry();
    ChainExecutorRegistry getChainExecutorsRegistry();
    HolderManager getHolderManager();

    DispatchedTriggerFactory getDispatchedTriggerFactory();


    default List<Listener> loadListeners(){
        return List.of(
                new HolderUpdaterListener(getPlugin())
        );
    }
    void startTasks();


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();

}
