package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FilterDamageCause extends Filter<Compilable.NoCompileData, Collection<String>> {
    public FilterDamageCause(@NotNull BossPlugin plugin) {
        super(plugin, "damage_cause");
    }

    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, NoCompileData compileData) {
        if(data.event() instanceof EntityDamageEvent event) {
            return value.contains(event.getCause().name().toUpperCase());
        }
        return true;
    }

    @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        return config.getStringList(key).stream().map(String::toUpperCase).toList();
    }
}
