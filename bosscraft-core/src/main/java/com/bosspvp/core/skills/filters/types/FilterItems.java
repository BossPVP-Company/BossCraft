package com.bosspvp.core.skills.filters.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FilterItems extends Filter<Collection<Material>, Collection<String>> {

    public FilterItems(BossPlugin plugin) {
        super(plugin,"items");
    }

    @Override
    public Collection<String> getValue(Config config, TriggerData data, String key) {
        return config.getStringList(key);
    }

    @Override
    protected boolean isMet(TriggerData data, Collection<String> value, Collection<Material> compileData) {
        ItemStack item = data.item();
        if(item == null) {
            return true;
        }
        return compileData.stream().anyMatch(it -> it==item.getType());
    }

    @Override
    public Collection<Material> makeCompileData(Config config, ViolationContext context, Collection<String> value) throws Exception {
        return value.stream().map(String::toUpperCase).map(Material::matchMaterial).filter(Objects::nonNull).toList();
    }
}
