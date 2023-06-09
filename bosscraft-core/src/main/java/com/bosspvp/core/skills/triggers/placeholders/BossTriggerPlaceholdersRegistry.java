package com.bosspvp.core.skills.triggers.placeholders;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholdersRegistry;
import com.bosspvp.api.skills.triggers.placeholders.types.*;
import com.bosspvp.core.skills.triggers.placeholders.types.*;
import org.jetbrains.annotations.NotNull;

public class BossTriggerPlaceholdersRegistry extends Registry<TriggerPlaceholder> implements TriggerPlaceholdersRegistry {
    private final BossPlugin plugin;
    public BossTriggerPlaceholdersRegistry(@NotNull BossPlugin plugin) {
        this.plugin = plugin;
        register(new TriggerPlaceholderDistance(plugin));
        register(new TriggerPlaceholderHits(plugin));
        register(new TriggerPlaceholderLocation(plugin));
        register(new TriggerPlaceholderText(plugin));
        register(new TriggerPlaceholderValue(plugin));
        register(new TriggerPlaceholderVictim(plugin));
    }

    @Override
    public Registry<TriggerPlaceholder> getRegistry() {
        return this;
    }

    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }
}