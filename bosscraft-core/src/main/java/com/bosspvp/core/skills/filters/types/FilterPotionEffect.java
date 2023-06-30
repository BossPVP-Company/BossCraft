package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public class FilterPotionEffect extends Filter<Compilable.NoCompileData, Collection<String>> {
    public FilterPotionEffect(@NotNull BossPlugin plugin) {
        super(plugin, "potion_effect");
    }

    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, NoCompileData compileData) {
        if(data.event() instanceof EntityPotionEffectEvent event) {
            PotionEffect effect = event.getNewEffect();
            if (effect == null) return false;
            return value.contains(effect.getType().getName());
        }
        return true;
    }

    @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        return config.getStringList(key).stream().map(String::toUpperCase).toList();
    }
}
