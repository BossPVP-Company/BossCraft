package com.bosspvp.core.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.conditions.ConditionsRegistry;
import com.bosspvp.api.skills.counters.CountersRegistry;
import com.bosspvp.api.skills.effects.EffectsRegistry;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentsRegistry;
import com.bosspvp.api.skills.effects.executors.ChainExecutorRegistry;
import com.bosspvp.api.skills.filters.FilterRegistry;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.mutators.MutatorRegistry;
import com.bosspvp.api.skills.triggers.TriggersRegistry;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholdersRegistry;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.core.skills.conditions.BossConditionsRegistry;
import com.bosspvp.core.skills.counters.BossCountersRegistry;
import com.bosspvp.core.skills.counters.CounterHandler;
import com.bosspvp.core.skills.effects.BossEffectsRegistry;
import com.bosspvp.core.skills.effects.arguments.BossEffectArgumentsRegistry;
import com.bosspvp.core.skills.effects.executors.BossChainExecutorRegistry;
import com.bosspvp.core.skills.filters.BossFilterRegistry;
import com.bosspvp.core.skills.mutators.BossMutatorRegistry;
import com.bosspvp.core.skills.triggers.BossTriggersRegistry;
import com.bosspvp.core.skills.triggers.placeholders.BossTriggerPlaceholdersRegistry;
import com.bosspvp.core.skills.visualeffects.BossVisualEffectsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    @Getter
    private FilterRegistry filterRegistry;
    @Getter
    private MutatorRegistry mutatorRegistry;
    @Getter
    private CountersRegistry countersRegistry;
    @Getter
    private VisualEffectsManager visualEffectsManager;
    public BossSkillsManager(BossPlugin plugin) {
        this.plugin = plugin;

    }

    @Override
    public void reload() {
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
        dispatchedTriggerFactory = new BossDTF();

        holderManager = new BossHolderManager(plugin);
        triggersRegistry = new BossTriggersRegistry(plugin);
        triggerPlaceholdersRegistry = new BossTriggerPlaceholdersRegistry(plugin);
        conditionsRegistry = new BossConditionsRegistry(plugin);
        effectsRegistry = new BossEffectsRegistry(plugin);
        effectArgumentsRegistry = new BossEffectArgumentsRegistry(plugin);
        chainExecutorsRegistry = new BossChainExecutorRegistry(plugin);
        filterRegistry = new BossFilterRegistry(plugin);
        mutatorRegistry = new BossMutatorRegistry(plugin);
        countersRegistry = new BossCountersRegistry(plugin);
        visualEffectsManager = new BossVisualEffectsManager(plugin);
    }


    @Override
    public @NotNull List<Listener> loadListeners() {
        return List.of(
                new HolderUpdaterListener(getPlugin()),
                new CounterHandler((BossCountersRegistry) countersRegistry)
        );
    }
}
