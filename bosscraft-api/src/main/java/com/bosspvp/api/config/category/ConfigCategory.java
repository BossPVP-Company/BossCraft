package com.bosspvp.api.config.category;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.exceptions.NotificationException;
import com.bosspvp.api.tuples.Pair;
import com.bosspvp.api.utils.FileUtils;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class ConfigCategory <T extends OkaeriConfig>{
    private final BossPlugin plugin;
    private final String id;
    private final String directory;
    private final boolean supportSubFolders;

    private final Class<T> configClass;

    /**
     * Config Category class
     *
     * @param plugin the plugin
     * @param id the category id
     * @param directory the directory path
     * @param supportSubFolders if accept configs from subFolders
     * @param configClass class of an okaeri config to use
     */
    public ConfigCategory(@NotNull BossPlugin plugin,
                          @NotNull String id,
                          @NotNull String directory,
                          boolean supportSubFolders,
                          @NotNull Class<T> configClass){
        this.plugin = plugin;
        this.id = id;
        this.directory = directory;
        this.supportSubFolders = supportSubFolders;
        this.configClass = configClass;
    }

    /**
     * Reload the config category
     *
     */
    public final void reload() throws NotificationException {
        beforeReload();
        clear();
        File dir = new File(plugin.getDataFolder(),directory);
        NotificationException.notifyFalse(dir.exists(),"&cDirectory '"+directory+"' doesn't exists!");
        for(Pair<String,File> entry : FileUtils.loadFiles(dir,supportSubFolders)){
            T conf =configClass.cast( eu.okaeri.configs.ConfigManager.create(
                    configClass,
                    (it)->{
                        it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
                        it.withBindFile(entry.getSecond());
                        it.load(true);
                    }
            ));
            acceptConfig(entry.getFirst(),conf);
        }
        afterReload();
    }

    /**
     * Clear the saved data
     *
     */
    protected abstract void clear();

    /**
     * Accept the config
     *
     */
    protected abstract void acceptConfig(@NotNull String id, @NotNull T config);

    /**
     * Called before category reload
     *<p></p>
     * Override to add implementation
     *
     */
    public void beforeReload(){
    }

    /**
     * Called after category reload
     *<p></p>
     * Override to add implementation
     *
     */
    public void afterReload(){
    }



    /**
     * Get the ID of a category
     *
     * @return The id
     */
    public final @NotNull String getId(){
        return id;
    }

    /**
     * Get the directory path
     *
     * @return The directory
     */
    public final @NotNull String getDirectory(){
        return directory;
    }


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    public final @NotNull BossPlugin getPlugin(){
        return plugin;
    }

}
