package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FilterPlayerName extends Filter<Compilable.NoCompileData, Collection<String>> {
    public FilterPlayerName(@NotNull BossPlugin plugin) {
        super(plugin,"player_name");
    }

    @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        return config.getStringList(key);
    }
    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, NoCompileData compileData) {
        var player = data.player();
        if(player==null) return true;
        for(var s : value) {
            if(player.getName().equalsIgnoreCase(s)) return true;
        }
        return false;
    }
}
