package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FilterVictimName extends Filter<Compilable.NoCompileData, Collection<String>> {
    public FilterVictimName(@NotNull BossPlugin plugin) {
        super(plugin,"victim_name");
    }

   @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        return config.getStringList(key);
    }
    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, NoCompileData compileData) {
        var victim = data.victim();
        if(victim==null) return true;
        for(var s : value) {
            if(victim.getName().equalsIgnoreCase(s)) return true;
        }
        return false;
    }
}
