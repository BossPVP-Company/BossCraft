package com.bosspvp.core.skills.filters;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.filters.Filter;
import com.bosspvp.api.skills.filters.FilterBlock;
import com.bosspvp.api.skills.filters.FilterList;
import com.bosspvp.api.skills.filters.FilterRegistry;
import com.bosspvp.api.skills.violation.ViolationContext;
import com.bosspvp.core.skills.filters.types.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BossFilterRegistry extends Registry<Filter<?, ?>> implements FilterRegistry {
    @Getter
    private final BossPlugin plugin;

    @Override
    public @NotNull FilterList compile(@NotNull Config config, @NotNull ViolationContext context) {
        List<FilterBlock<?, ?>> blocks = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            Filter<?, ?> filter = get(key);
            if (filter == null) {
                filter = get(key.replaceFirst("not_", ""));
                if (filter == null) {
                    continue;
                }
            }
            try {
                FilterBlock<?, ?> block = makeBlock(filter, config, context);
                if (block != null) {
                    blocks.add(block);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new FilterList(blocks);
    }

    @Nullable
    private <T, V> FilterBlock<T, V> makeBlock(Filter<T, V> filter,
                                               Config config,
                                               ViolationContext context) throws Exception {
        if (!filter.checkConfig(config, context)) {
            return null;
        }
        String configKey = config.hasPath("not_" + filter.getId()) ? "not_" + filter.getId() : filter.getId();
        var compileData = filter.makeCompileData(config, context, filter.getValue(config, null, configKey));
        return new FilterBlock<>(filter, config, compileData);
    }

    @Override
    public @NotNull Registry<Filter<?, ?>> getRegistry() {
        return this;
    }


    public BossFilterRegistry(@NotNull BossPlugin plugin) {
        this.plugin = plugin;

        register(new FilterBlocks(plugin));
        register(new FilterVictimName(plugin));
        register(new FilterPlayerName(plugin));
        register(new FilterPlayerPlaced(plugin));
        register(new FilterFullyGrown(plugin));
        register(new FilterFromSpawner(plugin));
        register(new FilterItems(plugin));
        register(new FilterDamageCause(plugin));
        register(new FilterPotionEffect(plugin));
        register(new FilterOnlyBosses(plugin));
        register(new FilterMaterials(plugin));
    }


}
