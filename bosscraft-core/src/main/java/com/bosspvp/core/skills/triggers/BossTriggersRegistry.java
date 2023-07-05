package com.bosspvp.core.skills.triggers;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerGroup;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.skills.triggers.TriggersRegistry;
import com.bosspvp.core.skills.triggers.types.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class BossTriggersRegistry extends Registry<Trigger> implements TriggersRegistry {
    @Getter
    private final BossPlugin plugin;
    private Registry<TriggerGroup> groupRegistry = new Registry<>();

    @Override
    public @Nullable Trigger get(@NotNull String id) {
        Trigger out = doGet(id);
        if (out == null) return null;
        out.setEnabled(true);
        return out;
    }

    @Override
    public void register(@NotNull TriggerGroup triggerGroup) {
        groupRegistry.register(triggerGroup);
    }

    @Override
    public Predicate<Trigger> withParameters(@NotNull Set<TriggerParameter> parameters) {
        return (it) -> {
            Set<TriggerParameter> triggerParameters = new HashSet<>();
            for(List<TriggerParameter> entry: it.getParameters().stream().map(
                    param -> {
                        List<TriggerParameter> out = new ArrayList<>(Arrays.asList(param.getInheritsFrom()));
                        out.add(param);
                        return out;
                    }).toList()){
                triggerParameters.addAll(entry);
            }
            return triggerParameters.containsAll(parameters);
        };
    }

    @Nullable
    private Trigger doGet(@NotNull String id) {
        for (TriggerGroup group : groupRegistry.values()) {
            if (id.startsWith(group.getPrefix() + "_")) {
                //clear prefix
                return group.create(id.substring(0, (group.getPrefix() + "_").length() - 1));
            }
        }

        return super.get(id);
    }

    @Override
    public Registry<Trigger> getRegistry() {
        return this;
    }

    public BossTriggersRegistry(@NotNull BossPlugin plugin) {
        this.plugin = plugin;
        register(new TriggerMove(plugin));
        register(new TriggerConsume(plugin));
        register(new TriggerClickEntity(plugin));
        register(new TriggerClickEntity(plugin));
        register(new TriggerMineBlock(plugin));
        register(new TriggerCatchFish(plugin));
        register(new TriggerEnchantItem(plugin));
        register(new TriggerKill(plugin));
        register(new TriggerTakeDamage(plugin));
        register(new TriggerBrew(plugin));
        register(new TriggerBrewIngredient(plugin));
        register(new TriggerBowAttack(plugin));
        register(new TriggerTridentAttack(plugin));
        register(new TriggerDamageItem(plugin));
        register(new TriggerTakeEntityDamage(plugin));
        register(new TriggerEntityItemDrop(plugin));
        register(new TriggerBlockItemDrop(plugin));
        register(new TriggerPotionEffect(plugin));
        register(new TriggerMeleeAttack(plugin));
    }
}

