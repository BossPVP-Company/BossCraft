package com.bosspvp.api.config.category;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.impl.BossConfigOkaeri;
import com.bosspvp.api.exceptions.NotificationException;
import com.bosspvp.api.tuples.Pair;
import com.bosspvp.api.utils.FileUtils;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public abstract class ConfigCategory <T extends BossConfigOkaeri>{
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
     * @param configClass class of a config to use
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
        if(!dir.exists()){
            loadDefaults();
        }
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
    private void loadDefaults(){
        for(String path : FileUtils.getAllPathsInResourceFolder(plugin,directory)){
            try {
                File file = new File(plugin.getDataFolder(), path);
                if(!file.getName().contains(".")){
                    file.mkdir();
                    plugin.getLogger().info("Dir: "+path +" | " +file.getName());
                    continue;
                }
                plugin.getLogger().info("File: "+path +" | " +file.getName());
                var stream = plugin.getResource(path);
                if(stream==null) continue;
                Files.copy(stream, Path.of(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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
