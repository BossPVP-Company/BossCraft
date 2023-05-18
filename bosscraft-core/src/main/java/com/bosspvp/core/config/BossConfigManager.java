package com.bosspvp.core.config;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.OkaeriConfigInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BossConfigManager implements ConfigManager {
    private final HashMap<String,OkaeriConfig> registry = new HashMap<>();

    private final BossPlugin plugin;
    public BossConfigManager(BossPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public void reloadAllConfigs() {
        registry.values().forEach(OkaeriConfig::load);
    }

    @Override
    public void reloadConfig(@NotNull String id) {
        OkaeriConfig config = getConfig(id);
        if(config==null) return;
        config.load();
    }

    @Override
    public void saveAllConfigs() {
        registry.values().forEach(OkaeriConfig::save);
    }

    @Override
    public void saveConfig(@NotNull String id) {
        OkaeriConfig config = getConfig(id);
        if(config==null) return;
        config.save();
    }

    @Override
    public @Nullable OkaeriConfig getConfig(@NotNull String id) {
        return registry.get(id);
    }

    @Override
    public OkaeriConfig addConfig(@NotNull String id, @NotNull Class<? extends OkaeriConfig> config, @NotNull OkaeriConfigInitializer initializer) {
        OkaeriConfig conf = eu.okaeri.configs.ConfigManager.create(
                config,
                initializer
        );
        registry.put(id, conf);
        return conf;
    }


    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }
}
