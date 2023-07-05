package com.bosspvp.api.config;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.category.ConfigCategory;
import com.bosspvp.api.config.impl.BossConfigOkaeri;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.OkaeriConfigInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**
 * ConfigManager
 * <p></p>
 * Manages BossPlugin configs.
 * Currently, uses {@link OkaeriConfig} only
 */
public interface ConfigManager {
    /**
     * reload all configs
     */
    void reloadAllConfigs();

    /**
     * reload the config
     *
     * @param id the name of a config
     */
    void reloadConfig(@NotNull String id);

    /**
     * Save all configs.
     */
    void saveAllConfigs();

    /**
     * Save the config
     *
     * @param id the name of a config
     */
    void saveConfig(@NotNull String id);

    /**
     * getEffectBuilder config added to the manager
     * or null if not found
     * @param id The name of a config
     * @return this
     */
    @Nullable
    BossConfigOkaeri getConfig(@NotNull String id);

    /**
     * Add new config to the handler
     *
     * @param config The config
     * @return created config
     */
    BossConfigOkaeri addConfig(@NotNull String id,
                               @NotNull Class<? extends BossConfigOkaeri> config,
                               @NotNull OkaeriConfigInitializer initializer);

    /**
     * reload all config categories
     */
    void reloadAllConfigCategories();

    /**
     * reload the config category
     *
     * @param id the id
     */
    void reloadConfigCategory(@NotNull String id);

    /**
     * getEffectBuilder config category added to the manager
     * or null if not found
     * @param id The id
     * @return The config category
     */
    @Nullable
    ConfigCategory<? extends OkaeriConfig> getConfigCategory(@NotNull String id);

    /**
     * Add new config category
     *
     * @param configCategory The config category
     */
    void addConfigCategory(@NotNull ConfigCategory<? extends OkaeriConfig> configCategory);

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
