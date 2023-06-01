package com.bosspvp.api.config.impl;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Config supporting Okaeri lib
 */
public class BossConfigOkaeri extends OkaeriConfig {
    private Config config;

    public BossConfigOkaeri(){
        if(!(getConfigurer() instanceof YamlBukkitConfigurer)) {
            throw new IllegalArgumentException("Tried to apply unsupported okaeri configurer for BossConfig");
        }
        try {
            Field configField = getConfigurer().getClass().getDeclaredField("config");
            configField.setAccessible(true);
            YamlConfiguration conf = (YamlConfiguration) configField.get(getConfigurer());
            this.config = BossAPI.getInstance().createDelegatedConfig(conf,conf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public @Nullable Config getSubsection(@NotNull String path){
        return config.getSubsection(path);
    }
    public @NotNull Config asConfig(){
        return config;
    }
}
