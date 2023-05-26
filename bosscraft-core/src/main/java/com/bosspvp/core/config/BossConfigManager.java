package com.bosspvp.core.config;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.BossConfig;
import com.bosspvp.api.config.ConfigManager;
import com.bosspvp.api.config.category.ConfigCategory;
import com.bosspvp.api.exceptions.NotificationException;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.OkaeriConfigInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BossConfigManager implements ConfigManager {
    private final HashMap<String,BossConfig> configRegistry = new HashMap<>();
    private final HashMap<String,ConfigCategory<? extends OkaeriConfig>> configCategoryRegistry = new HashMap<>();

    private final BossPlugin plugin;
    public BossConfigManager(BossPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public void reloadAllConfigs() {
        configRegistry.values().forEach(OkaeriConfig::load);
    }

    @Override
    public void reloadConfig(@NotNull String id) {
        OkaeriConfig config = getConfig(id);
        if(config==null) return;
        config.load();
    }

    @Override
    public void saveAllConfigs() {
        configRegistry.values().forEach(OkaeriConfig::save);
    }

    @Override
    public void saveConfig(@NotNull String id) {
        OkaeriConfig config = getConfig(id);
        if(config==null) return;
        config.save();
    }

    @Override
    public @Nullable BossConfig getConfig(@NotNull String id) {
        return configRegistry.get(id);
    }

    @Override
    public BossConfig addConfig(@NotNull String id, @NotNull Class<? extends BossConfig> config, @NotNull OkaeriConfigInitializer initializer) {
        BossConfig conf = eu.okaeri.configs.ConfigManager.create(
                config,
                initializer
        );
        configRegistry.put(id, conf);
        return conf;
    }

    @Override
    public void reloadAllConfigCategories() {
       for(ConfigCategory<? extends OkaeriConfig> entry : configCategoryRegistry.values()){
           try {
               entry.reload();
           }catch (NotificationException exception){
               plugin.getLogger().info("&eException while trying to " +
                       "reload the config category with id: "+entry.getId()
                       +"\nMessage: "+exception.getMessage()
               );
           }
       }
    }

    @Override
    public void reloadConfigCategory(@NotNull String id) {
        try {
            ConfigCategory<? extends OkaeriConfig> config = configCategoryRegistry.get(id);
            if(config == null) return;
            config.reload();
        }catch (NotificationException exception){
            plugin.getLogger().info("&eException while trying to " +
                    "reload the config category with id: "+id
                    +"\nMessage: "+exception.getMessage()
            );
        }
    }

    @Override
    public @Nullable ConfigCategory<? extends OkaeriConfig> getConfigCategory(@NotNull String id) {
        return configCategoryRegistry.get(id);
    }

    @Override
    public void addConfigCategory(@NotNull ConfigCategory<? extends OkaeriConfig> configCategory) {
        configCategoryRegistry.put(configCategory.getId(),configCategory);
    }


    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }
}
