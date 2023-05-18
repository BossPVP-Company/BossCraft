package com.bosspvp.api;

import com.bosspvp.api.commands.BossCommand;
import com.bosspvp.api.config.ConfigManager;
import com.bosspvp.api.config.ConfigSettings;
import com.bosspvp.api.config.LangSettings;
import com.bosspvp.api.events.EventManager;
import com.bosspvp.api.schedule.Scheduler;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class BossPlugin extends JavaPlugin {
    private final Logger logger;
    private final EventManager eventManager;
    private final Scheduler scheduler;
    private final ConfigManager configManager;

    private final OkaeriConfig configYml;
    private final OkaeriConfig langYml;

    public BossPlugin(){
        BossAPI api = getAPI();
        if(api==null){
            api = loadAPI();
            BossAPI.Instance.set(api);
        }
        logger = api.createLogger(this);

        getLogger().info("Initializing "  + this.getName());

        eventManager = api.createEventManager(this);
        scheduler = api.createScheduler(this);
        configManager = api.createConfigManager(this);

        configYml = createConfig();
        langYml = createLang();
    }


    @Override
    public final void onEnable() {
        this.getLogger().info("");
        this.getLogger().info("Enabling " + this.getName());

        this.loadListeners().forEach(listener -> getEventManager().registerListener(listener));

        this.loadPluginCommands().forEach(BossCommand::register);

        this.handleEnable();

        this.getScheduler().runLater(1,this::afterLoad);

        this.getLogger().info("");
    }



    @Override
    public final void onDisable() {
        super.onDisable();

        this.getEventManager().unregisterAllListeners();
        this.getScheduler().cancelAll();

        this.handleDisable();

        this.getLogger().info("Cleaning up...");
    }


    @Override
    public final void onLoad() {
        super.onLoad();

        this.handleLoad();
    }



    public final void afterLoad() {

        this.handleAfterLoad();

        this.reload();

        this.getLogger().info("Loaded " + this.getName());
    }



    public final void reload() {
        this.getScheduler().cancelAll();
        this.getConfigManager().reloadAllConfigs();

        this.handleReload();
    }

    /**
     * Method with empty implementation,
     * that is called on plugin disable
     * right after registering commands and listeners
     * <p></p>
     * Override to add implementation
     *
     */
    protected abstract void handleEnable();
    /**
     * Method with empty implementation,
     * that is called on plugin disable
     * right after cancelling all tasks
     * and unregistering listeners
     * <p></p>
     * Override to add implementation
     *
     */
    protected abstract void handleDisable();

    /**
     * Method with empty implementation,
     * that is called on plugin load.
     * <p></p>
     * Override to add implementation
     *
     */
    protected void handleLoad() {
    }

    /**
     * Method with empty implementation,
     * that is called on plugin reload
     * right after cancelling all tasks and
     * reloading the configs
     * <p></p>
     * Override to add implementation
     *
     */
    protected void handleReload() {

    }

    /**
     * Method with empty implementation,
     * that is called on the next tick
     * after plugin has been enabled
     * <p></p>
     * Override to add implementation
     *
     */
    protected void handleAfterLoad() {
    }



    /**
     * All listeners to be registered.
     * <p></p>
     * Override to add implementation
     *
     * @return A list of all listeners.
     */
    protected List<Listener> loadListeners()  {
        return new ArrayList<>();
    }

    /**
     * All commands to be registered.
     * <p></p>
     * Override to add implementation
     *
     * @return A list of all commands
     */
    protected List<BossCommand> loadPluginCommands() {
        return new ArrayList<>();
    }



    /**
     * Get plugin logger
     *
     * @return plugin logger
     */
    @NotNull @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * Get Scheduler
     * <p></p>
     * Works via Bukkit#getScheduler
     * but has more convenient methods
     *
     * @return The scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Get Event Manager
     *
     * @return The event manager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Get config manager
     *
     * @return config manager
     */
    @NotNull
    public ConfigManager getConfigManager() {
        return configManager;
    }



    /**
     * Creates LangYml
     * <p>
     * Override if needed.
     *
     * @return lang.yml.
     */
    protected OkaeriConfig createLang() {

        return  configManager.addConfig(
                "lang",
                LangSettings.class,
                (it)->{
                    it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
                    it.withBindFile(new File(getDataFolder(),"lang.yml"));
                    it.saveDefaults();
                    it.load(true);
                });
    }

    /**
     * Creates ConfigYml
     * <p>
     * Override if needed
     *
     * @return config.yml.
     */
    protected OkaeriConfig createConfig() {
        return  configManager.addConfig(
                "config",
                ConfigSettings.class,
                (it)->{
                    it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
                    it.withBindFile(new File(getDataFolder(),"config.yml"));
                    it.saveDefaults();
                    it.load(true);
                });
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The BossPlugin doesn't support it.
     *
     * @deprecated Use the Boss config system.
     * @see BossPlugin#getConfigManager()
     */
    @NotNull
    @Override
    @Deprecated
    public final FileConfiguration getConfig() {
        this.getLogger().warning("Call to Bukkit config method in Boss plugin!");

        return super.getConfig();
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The BossPlugin doesn't support it.
     *
     * @deprecated Use the Boss config system.
     * @see BossPlugin#getConfigManager()
     */
    @Override
    @Deprecated
    public final void saveConfig() {
        this.getLogger().warning("Call to Bukkit config method in Boss plugin!");

        super.saveConfig();
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The BossPlugin doesn't support it.
     *
     * @deprecated Use the Boss config system.
     * @see BossPlugin#getConfigManager()
     */
    @Override
    @Deprecated
    public final void saveDefaultConfig() {
        this.getLogger().warning("Call to Bukkit config method in Boss plugin!");
        super.saveDefaultConfig();
    }

    /**
     * The {@link JavaPlugin} method
     * <p></p>
     * The BossPlugin doesn't support it.
     *
     * @deprecated Use the Boss config system.
     * @see BossPlugin#getConfigManager()
     */
    @Override
    @Deprecated
    public final void reloadConfig() {
        this.getLogger().warning("Call to default Bukkit method in Boss plugin!");

        super.reloadConfig();
    }


    /**
     * Get ConfigYml
     *
     * @return The config
     */
    @NotNull
    public OkaeriConfig getConfigYml() {
        return configYml;
    }

    /**
     * Get LangYml
     *
     * @return The langYml
     */
    @NotNull
    public OkaeriConfig getLangYml() {
        return langYml;
    }


    /**
     * Get Lang settings
     * <p></p>
     * It is a class with settings for api.
     * Important: You have to override this method if you made
     * your own class for lang.yml
     *
     * @return The langYml
     */
    @NotNull
    public LangSettings getLangSettings() {
        Validate.isTrue(
                langYml instanceof LangSettings,
                "Failed to use BossPlugin#getLangYml as LangSettings.class"+
                        "  Override this method if you have your own class for lang.yml"
        );
        return (LangSettings) langYml;
    }
    /**
     * Get Lang settings
     * <p></p>
     * It is a class with settings for api.
     * Important: You have to override this method if you made
     * your own class for lang.yml
     *
     * @return The langYml
     */
    @NotNull
    public ConfigSettings getConfigSettings() {
        Validate.isTrue(
                langYml instanceof LangSettings,
                "Failed to use BossPlugin#getConfigYml as ConfigSettings.class"+
                        "  Override this method if you have your own class for config.yml"
        );
        return (ConfigSettings) configYml;
    }

    /**
     * Load an API. Use it if you want to implement
     * your own BossAPI class.
     * <p></p>
     *
     * @return BossAPI
     */
    protected BossAPI loadAPI(){
        return BossAPI.getInstance();
    }

    /**
     * Get BossAPI instance
     *
     * @return BossAPI instance
     */
    public BossAPI getAPI() {
        return BossAPI.getInstance();
    }
}
