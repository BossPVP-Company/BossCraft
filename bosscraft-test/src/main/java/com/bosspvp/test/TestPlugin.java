package com.bosspvp.test;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.commands.BossCommand;
import com.bosspvp.api.config.impl.BossConfigOkaeri;
import com.bosspvp.api.config.impl.ConfigSettings;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.skills.holder.HolderProvider;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.bosspvp.api.skills.holder.provided.SimpleProvidedHolder;
import com.bosspvp.core.BossAPIImpl;
import com.bosspvp.core.events.listeners.EntityDeathListener;
import com.bosspvp.test.commands.CommandTest;
import com.bosspvp.test.config.ConfigFileOkaeri;
import com.bosspvp.test.config.category.CategoryTest;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestPlugin extends BossPlugin {
    //instance
    @Getter
    private static TestPlugin instance;
    @Getter
    private CategoryTest categoryTest;

    public TestPlugin() {
        instance = this;
    }

    @Override
    protected void handleLoad() {
        categoryTest = new CategoryTest(this);
        getConfigManager().addConfigCategory(categoryTest);
    }

    @Override
    protected void handleEnable() {
        getGuiController().enableUpdater(true);
        getSkillsManager().getHolderManager().registerHolderProvider(
                player -> {
                    Holder holder = categoryTest.getSkillsHolder(player);
                    if(holder == null) {
                        return null;
                    }
                    ProvidedHolder providedHolder = new SimpleProvidedHolder(
                            holder
                    );
                    return Collections.singletonList(providedHolder);
                }
        );

    }


    @Override
    protected void handleDisable() {

    }


    //make your own class for config.yml or lang.yml
    @Override
    protected BossConfigOkaeri createConfig() {
        return getConfigManager().addConfig("config",
                ConfigFileOkaeri.class,
                (it) -> {
                    it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
                    it.withBindFile(new File(getDataFolder(), "config.yml"));
                    it.saveDefaults();
                    it.load(true);
                });
    }

    @Override
    public @NotNull ConfigSettings getConfigSettings() {
        //obtain ConfigSettings variable from your config class
        return ((ConfigFileOkaeri) getConfigManager().getConfig("config")).getSettings();
    }


    //commands
    @Override
    protected List<BossCommand> loadPluginCommands() {
        return List.of(new CommandTest(this));
    }


    //HAS To BE ADDED FOR CORE PLUGIN
    @Override
    protected List<Listener> loadListeners() {
        return List.of(
                new EntityDeathListener(this)
        );
    }

    //load implementation.
    // The plugin which loads an API has to be enabled first
    @Override
    public BossAPI loadAPI() {
        return new BossAPIImpl(this);
    }
}
