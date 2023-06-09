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
import com.bosspvp.core.skills.conditions.BossConditionsRegistry;
import com.bosspvp.core.skills.effects.BossEffectsRegistry;
import com.bosspvp.core.skills.effects.arguments.BossEffectArgumentsRegistry;
import com.bosspvp.core.skills.effects.executors.BossChainExecutorRegistry;
import com.bosspvp.core.skills.triggers.BossTriggersRegistry;
import com.bosspvp.core.skills.triggers.placeholders.BossTriggerPlaceholdersRegistry;
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
        holderManager = new BossHolderManager(plugin);
        triggersRegistry = new BossTriggersRegistry(plugin);
        triggerPlaceholdersRegistry = new BossTriggerPlaceholdersRegistry(plugin);
        conditionsRegistry = new BossConditionsRegistry(plugin);
        effectsRegistry = new BossEffectsRegistry(plugin);
        effectArgumentsRegistry = new BossEffectArgumentsRegistry(plugin);
        chainExecutorsRegistry = new BossChainExecutorRegistry(plugin);
        dispatchedTriggerFactory = new BossDTF();
    }
}
