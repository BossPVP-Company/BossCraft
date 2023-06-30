package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Boss;
import org.bukkit.entity.ElderGuardian;
import org.jetbrains.annotations.NotNull;

public class FilterOnlyBosses extends Filter<Compilable.NoCompileData,Boolean> {
    public FilterOnlyBosses(@NotNull BossPlugin plugin) {
        super(plugin, "only_bosses");
    }

    @Override
    protected boolean isMet(TriggerData data, Boolean value, NoCompileData compileData) {
        var entity = data.victim();
        if (entity == null) return true;
        return value == ((entity instanceof Boss) || (entity instanceof ElderGuardian));
    }

    @Override
    public Boolean getValue(Config config, TriggerData data, String key) {
        return config.getBool(key);
    }
}
