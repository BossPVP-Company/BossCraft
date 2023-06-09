package com.bosspvp.core.config;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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


    @Override
    public @Nullable BossConfig getSubsection(@NotNull String path){
        ConfigurationSection section = handle.getConfigurationSection(path);
        if(section==null) return null;
        BossConfig out = new BossConfig(yamlHandle,section);
        out.addInjectablePlaceholder(injections);
        return out;
    }

    @Override
    public @Nullable List<Config> getSubsectionListOrNull(@NotNull String path) {
        Object obj = handle.get(path);
        if(obj==null) return null;
        if(!(obj instanceof ConfigurationSection mainSection)) return null;
        List<ConfigurationSection> list = mainSection.getKeys(false).stream()
                .map(mainSection::getConfigurationSection).filter(Objects::nonNull).toList();
        List<Config> out = new ArrayList<>();
        list.forEach(it->{
            BossConfig config = new BossConfig(yamlHandle,it);
            config.addInjectablePlaceholder(injections);
            out.add(config);
        });
        return out;
    }

    @Override
    public double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context) {
        String text = getStringOrNull(path);
        if(text==null) return 0;
        return BossAPI.getInstance().evaluate(text,context);
    }

    @Override
    public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        placeholders.forEach(it->injections.add(it));
    }

    @Override
    public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        placeholders.forEach(it->injections.remove(it));
    }

    @Override
    public void clearInjectedPlaceholders() {
        injections.clear();
    }

    @Override
    public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
        return injections;
    }

    @Override
    public boolean hasPath(@NotNull String path){
        return handle.contains(path);
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BossConfig other)) return false;
        return handle.equals(other.handle) && yamlHandle.equals(other.yamlHandle);
    }

    @Override
    public int hashCode() {
        return handle.hashCode() + yamlHandle.hashCode();
    }
}
