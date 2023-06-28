package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;

public class FilterFullyGrown extends Filter<Compilable.NoCompileData,Boolean> {
    public FilterFullyGrown(BossPlugin plugin) {
        super(plugin,"fully_grown");
    }

    @Override
    protected boolean isMet(TriggerData data, Boolean value, NoCompileData compileData) {
        var block = data.block();
        if (block == null) {
            return true;
        }
        var blockData = block.getBlockData();
        boolean isFullyGrown = true;
        if (blockData instanceof org.bukkit.block.data.Ageable ageable) {
            isFullyGrown = ageable.getAge() == ageable.getMaximumAge();
        }
        return isFullyGrown == value;
    }

    @Override
    public Boolean getValue(Config config, TriggerData data, String key) {
        return config.getBool(key);
    }
}
