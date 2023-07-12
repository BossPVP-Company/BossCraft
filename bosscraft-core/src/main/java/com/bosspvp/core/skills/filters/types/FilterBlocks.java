package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterBlocks extends Filter<Compilable.NoCompileData, Collection<String>> {
    public FilterBlocks(@NotNull BossPlugin plugin) {
        super(plugin,"blocks");
    }
    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, NoCompileData compileData) {
        var block = data.block();
        if(block == null) return true;

        return value.stream().anyMatch(s -> block.getType().name().equalsIgnoreCase(s));
    }

    @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        List<String> list = new ArrayList<>();

        for(String s : config.getStringList(key)){
            if(s.startsWith("*")){
                String preMaterial = s.split("\\*")[1].toUpperCase();
                for(Material material : Material.values()){
                    if(material.name().contains(preMaterial)){
                        list.add(material.name());
                    }
                }
                continue;
            }
            list.add(s.toUpperCase());
        }

        return list;
    }
}
