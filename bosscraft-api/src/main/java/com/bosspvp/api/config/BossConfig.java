package com.bosspvp.api.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;


public class BossConfig extends OkaeriConfig {
    private YamlConfiguration handle;

    public BossConfig(){
        if(!(getConfigurer() instanceof YamlBukkitConfigurer)) {
            throw new IllegalArgumentException("Tried to apply unsupported okaeri configurer for BossConfig");
        }
        try {
            Field configField = getConfigurer().getClass().getDeclaredField("config");
            configField.setAccessible(true);
            this.handle = (YamlConfiguration) configField.get(getConfigurer());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean has(@NotNull String path){
        return get(path) != null;
    }

    public @Nullable ConfigurationSection getSubsection(@NotNull String path){
        return handle.getConfigurationSection(path);
    }
    public @NotNull ConfigurationSection asSection(){
        return handle;
    }
}
