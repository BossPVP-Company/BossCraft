package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FilterMaterials extends Filter<Compilable.NoCompileData, Collection<String>> {
    public FilterMaterials(@NotNull BossPlugin plugin) {
        super(plugin,"materials");
    }
    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, NoCompileData compileData) {
        var mat = data.material();
        if(mat == null) return true;

        return value.stream().anyMatch(s -> mat.name().equalsIgnoreCase(s));
    }

    @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        return config.getStringList(key);
    }
}
