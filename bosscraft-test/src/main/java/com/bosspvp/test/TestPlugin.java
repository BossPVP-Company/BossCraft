package com.bosspvp.test;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.commands.BossCommand;
import com.bosspvp.api.config.ConfigSettings;
import com.bosspvp.core.BossAPIImpl;
import com.bosspvp.test.commands.CommandTest;
import com.bosspvp.test.config.ConfigFile;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class TestPlugin extends BossPlugin {
    @Override
    protected void handleEnable() {

    }

    @Override
    protected void handleDisable() {

    }


    //make your own class for config.yml or lang.yml
    @Override
    protected OkaeriConfig createConfig() {
        return getConfigManager().addConfig("config",
                ConfigFile.class,
                (it)->{
                    it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
                    it.withBindFile(new File(getDataFolder(),"config.yml"));
                    it.saveDefaults();
                    it.load(true);
                });
    }
    @Override
    public @NotNull ConfigSettings getConfigSettings() {
        //obtain ConfigSettings variable from your config class
        return ((ConfigFile)getConfigManager().getConfig("config")).getSettings();
    }


    //commands
    @Override
    protected List<BossCommand> loadPluginCommands() {
        return List.of(new CommandTest(this));
    }


    //load implementation.
    // The plugin which loads an API has to be enabled first
    @Override
    public BossAPI loadAPI() {
        return new BossAPIImpl();
    }
}