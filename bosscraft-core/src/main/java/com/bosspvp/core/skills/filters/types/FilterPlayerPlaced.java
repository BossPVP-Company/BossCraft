package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

public class FilterPlayerPlaced extends Filter<Compilable.NoCompileData, Boolean> {
    public FilterPlayerPlaced(@NotNull BossPlugin plugin) {
        super(plugin, "player_placed");
    }

    @Override
    protected boolean isMet(TriggerData data, Boolean value, NoCompileData compileData) {
        var block = data.block();
        if(block==null) return true;

        //@TODO
        return true;
    }

    @Override
    public Boolean getValue(Config config, TriggerData data, String key) {
        return config.getBool(key);
    }
}
