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
    private final HolderManager holderManager;
    @Getter
    private final TriggersRegistry triggersRegistry;
    @Getter
    private final TriggerPlaceholdersRegistry triggerPlaceholdersRegistry;
    @Getter
    private final ConditionsRegistry conditionsRegistry;
    @Getter
    private final EffectsRegistry effectsRegistry;
    @Getter
    private final BossDTF dispatchedTriggerFactory;
    @Getter
    private final ChainExecutorRegistry chainExecutorsRegistry;
    @Getter
    private final EffectArgumentsRegistry effectArgumentsRegistry;
    public BossSkillsManager(BossPlugin plugin) {
        this.plugin = plugin;
        holderManager = new HolderManager();
        triggersRegistry = new TriggersRegistry();
        triggerPlaceholdersRegistry = new TriggerPlaceholdersRegistry();
        conditionsRegistry = new ConditionsRegistry();
        effectsRegistry = new EffectsRegistry();
        effectArgumentsRegistry = new EffectArgumentsRegistry();
        chainExecutorsRegistry = new ChainExecutorRegistry();
        dispatchedTriggerFactory = new BossDTF();

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
}
