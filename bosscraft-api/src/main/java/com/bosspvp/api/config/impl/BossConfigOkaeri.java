package com.bosspvp.api.config.impl;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Config supporting Okaeri lib
 */
public class BossConfigOkaeri extends OkaeriConfig {
    @Exclude
    private Config config;


    public @Nullable Config getSubsection(@NotNull String path){
        if(config == null) obtainConfig();
        return config.getSubsection(path);
    }
    public @NotNull Config asConfig(){
        if(config == null) obtainConfig();
        return config;
    }

    //Why not set config in a constructor?
    // Because getConfigurer() returns null there
    private void obtainConfig(){
        if(!(getConfigurer() instanceof OkaeriValidator wrapper)) {
            throw new IllegalArgumentException("Tried to apply unsupported okaeri configurer wrapper for BossConfig. " +
                    "Configurer Class: " + getConfigurer().getClass().getName());
        }
        if(!(wrapper.getWrapped() instanceof YamlBukkitConfigurer configurer)) {
            throw new IllegalArgumentException("Tried to apply unsupported okaeri wrapped configurer for BossConfig. " +
                    "Wrapped Configurer Class: " + wrapper.getWrapped().getClass().getName());
        }
        try {
            Field configField = configurer.getClass().getDeclaredField("config");
            configField.setAccessible(true);
            YamlConfiguration conf = (YamlConfiguration) configField.get(configurer);
            //For an unknown for me reason, the okaeri changes the path separator to char symbol 29
            //That caused the YamlConfiguration to ignore '.' in the path
            conf.options().pathSeparator('.');
            this.config = BossAPI.getInstance().createDelegatedConfig(conf,conf);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
