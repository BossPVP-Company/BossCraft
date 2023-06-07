package com.bosspvp.api.skills.triggers.placeholders;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.triggers.placeholders.types.*;
import org.jetbrains.annotations.NotNull;

public class TriggerPlaceholdersRegistry extends Registry<TriggerPlaceholder> {
    public TriggerPlaceholdersRegistry(@NotNull BossPlugin plugin){
        register(new TriggerPlaceholderDistance(plugin));
        register(new TriggerPlaceholderHits(plugin));
        register(new TriggerPlaceholderLocation(plugin));
        register(new TriggerPlaceholderText(plugin));
        register(new TriggerPlaceholderValue(plugin));
        register(new TriggerPlaceholderVictim(plugin));
    }
}
