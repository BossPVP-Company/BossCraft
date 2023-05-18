package com.bosspvp.api;

import com.bosspvp.api.commands.BossCommand;
import com.bosspvp.api.events.EventManager;
import com.bosspvp.api.schedule.Scheduler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class BossPlugin extends JavaPlugin {
    private final Logger logger;
    private final EventManager eventManager;
    private final Scheduler scheduler;

    public BossPlugin(){
        BossAPI api = getAPI();
        logger = api.createLogger(this);

        getLogger().info("Initializing "  + this.getName());

        eventManager = api.createEventManager(this);
        scheduler = api.createScheduler(this);
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
     * Get plugin logger
     *
     * @return plugin logger
     */
    @NotNull @Override
    public Logger getLogger() {
        return logger;
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
