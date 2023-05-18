package com.bosspvp.api.config;

import com.bosspvp.api.BossPlugin;
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
     * @param name the name of a config
     */
    void reloadConfig(@NotNull String name);

    /**
     * Save all configs.
     */
    void saveAllConfigs();

    /**
     * Save the specified config
     *
     * @param name the name of a config
     */
    void saveConfig(@NotNull String name);

    /**
     * get config added to the manager
     * or null if not found
     * @param name The name of a config
     * @return this
     */
    @Nullable
    LoadableConfig getConfig(@NotNull String name);
    /**
     * Add new config to the handler
     *
     * @param config The loadable config
     * @return this
     */
    ConfigManager addConfig(@NotNull LoadableConfig config);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
