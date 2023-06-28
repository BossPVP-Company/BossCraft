package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

public class FilterFromSpawner extends Filter<Compilable.NoCompileData, Boolean> {
    public FilterFromSpawner(@NotNull BossPlugin plugin) {
        super(plugin, "from_spawner");
    }

    @Override
    protected boolean isMet(TriggerData data, Boolean value, NoCompileData compileData) {
        var victim = data.victim();
        if (victim == null) {
            return true;
        }
        //paper
        return value == victim.fromMobSpawner();
    }

    @Override
    public Boolean getValue(Config config, TriggerData data, String key) {
        return config.getBool(key);
    }
}
