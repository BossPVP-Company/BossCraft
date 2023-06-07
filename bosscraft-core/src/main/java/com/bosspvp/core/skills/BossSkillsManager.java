package com.bosspvp.core.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.conditions.ConditionsRegistry;
import com.bosspvp.api.skills.effects.EffectsRegistry;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentsRegistry;
import com.bosspvp.api.skills.effects.executors.ChainExecutorRegistry;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.triggers.TriggersRegistry;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholdersRegistry;
import lombok.Getter;
import org.bukkit.Bukkit;

public class BossSkillsManager implements SkillsManager {
    @Getter
    private final BossPlugin plugin;
    @Getter
    private HolderManager holderManager;
    @Getter
    private TriggersRegistry triggersRegistry;
    @Getter
    private TriggerPlaceholdersRegistry triggerPlaceholdersRegistry;
    @Getter
    private ConditionsRegistry conditionsRegistry;
    @Getter
    private EffectsRegistry effectsRegistry;
    @Getter
    private BossDTF dispatchedTriggerFactory;
    @Getter
    private ChainExecutorRegistry chainExecutorsRegistry;
    @Getter
    private EffectArgumentsRegistry effectArgumentsRegistry;
    public BossSkillsManager(BossPlugin plugin) {
        this.plugin = plugin;

    }

    @Override
    public void startTasks() {
        getPlugin().getScheduler().runTimer(20, 20, () -> {
            HolderManager holderManager = getHolderManager();
            for (var player : Bukkit.getOnlinePlayers()) {
                holderManager.refreshHolders(player);
            }
        });
        dispatchedTriggerFactory.startTicking(getPlugin());
    }

    @Override
    public void init() {
        holderManager = new HolderManager();
        triggersRegistry = new TriggersRegistry(plugin);
        triggerPlaceholdersRegistry = new TriggerPlaceholdersRegistry(plugin);
        conditionsRegistry = new ConditionsRegistry(plugin);
        effectsRegistry = new EffectsRegistry(plugin);
        effectArgumentsRegistry = new EffectArgumentsRegistry(plugin);
        chainExecutorsRegistry = new ChainExecutorRegistry();
        dispatchedTriggerFactory = new BossDTF();
    }
}
