package com.bosspvp.core.config;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.InjectablePlaceholderList;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.Crunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class BossConfig implements Config {
    @Getter
    private YamlConfiguration yamlHandle;
    @Getter
    private ConfigurationSection handle;

    private List<InjectablePlaceholder> injections = new ArrayList<>();

    public BossConfig(YamlConfiguration yamlHandle,
                      ConfigurationSection handle){
        this.yamlHandle = yamlHandle;
        this.handle = handle;
    }
    @Override
    public @NotNull Set<String> getKeys(boolean deep){
        return handle.getKeys(deep);
    }
    @Override
    public @Nullable Object get(@NotNull String path){
        return handle.get(path);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object obj) {
        handle.set(path,obj);
    }

    @Override
    public @Nullable Integer getIntOrNull(@NotNull String path) {
        Object result = handle.get(path);
        if(!(result instanceof Integer out)) return null;
        return out;
    }

    @Override
    public @Nullable List<Integer> getIntListOrNull(@NotNull String path) {
        List<Integer> out = handle.getIntegerList(path);
        if(out.isEmpty()) return null;
        return out;
    }

    @Override
    public @Nullable Boolean getBoolOrNull(@NotNull String path) {
        Object result = handle.get(path);
        if(!(result instanceof Boolean out)) return null;
        return out;
    }

    @Override
    public @Nullable List<Boolean> getBoolListOrNull(@NotNull String path) {
        List<Boolean> out = handle.getBooleanList(path);
        if(out.isEmpty()) return null;
        return out;
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull String path) {
        return handle.getString(path);
    }

    @Override
    public @Nullable List<String> getStringListOrNull(@NotNull String path) {
        return handle.getStringList(path);
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull String path) {
        Object result = handle.get(path);
        if(!(result instanceof Double out)) return null;
        return out;
    }

    @Override
    public @Nullable List<Double> getDoubleListOrNull(@NotNull String path) {
        List<Double> out = handle.getDoubleList(path);
        if(out.isEmpty()) return null;
        return out;
    }


    public @Nullable BossConfig getSubsection(@NotNull String path){
        ConfigurationSection section = handle.getConfigurationSection(path);
        if(section==null) return null;
        BossConfig out = new BossConfig(yamlHandle,section);
        out.addInjectablePlaceholders(injections);
        return out;
    }

    @Override
    public double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context) {
        return BossAPI.getInstance().evaluate(getString(path),context);
    }

    @Override
    public void addInjectablePlaceholders(List<InjectablePlaceholder> placeholders) {
        injections.addAll(placeholders);
    }

    @Override
    public List<InjectablePlaceholder> getInjectedPlaceholders() {
        return injections;
    }

    @Override
    public void clearInjectedPlaceholders() {
        injections.clear();
    }

    @Override
    public boolean hasPath(@NotNull String path){
        return handle.contains(path);
    }



}
