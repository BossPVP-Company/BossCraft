package com.bosspvp.api.config;

import com.bosspvp.api.BossPlugin;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.OkaeriConfigInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigManager {
    /**
     * load all configs from disk
     */
    void reloadAllConfigs();

    /**
     * load the specified config from disk
     *
     * @param id the name of a config
     */
    void reloadConfig(@NotNull String id);

    /**
     * Save all configs.
     */
    void saveAllConfigs();

    /**
     * Save the specified config
     *
     * @param id the name of a config
     */
    void saveConfig(@NotNull String id);

    /**
     * get config added to the manager
     * or null if not found
     * @param id The name of a config
     * @return this
     */
    @Nullable
    OkaeriConfig getConfig(@NotNull String id);

    /**
     * Add new config to the handler
     *
     * @param config The loadable config
     * @return created config
     */
    OkaeriConfig addConfig(@NotNull String id,
                           @NotNull Class<? extends OkaeriConfig> config,
                           @NotNull OkaeriConfigInitializer initializer);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
