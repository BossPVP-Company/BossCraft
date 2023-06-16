package com.bosspvp.core.config;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmptyConfig implements Config {
    private List<InjectablePlaceholder> injections;
    YamlConfiguration yamlHandle;
    public EmptyConfig(YamlConfiguration yamlHandle, List<InjectablePlaceholder> injections){
        this.yamlHandle = yamlHandle;
        this.injections = injections;
    }
    @Override
    public boolean hasPath(@NotNull String path) {
        return false;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return new HashSet<>();
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object obj) {

    }

    @Override
    public @Nullable Integer getIntOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<Integer> getIntListOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable Boolean getBoolOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<Boolean> getBoolListOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<String> getStringListOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable Double getDoubleOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<Double> getDoubleListOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @NotNull Config getSubsection(@NotNull String path) {
        return new EmptyConfig(yamlHandle,injections);
    }

    @Override
    public @Nullable Config getSubsectionOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<Config> getSubsectionListOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context) {
        return 0;
    }

    @Override
    public @NotNull ConfigurationSection getHandle() {
        return yamlHandle;
    }

    @Override
    public @NotNull ConfigurationSection getYamlHandle() {
        return yamlHandle;
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
    public boolean equals(Object obj) {
        if(!(obj instanceof EmptyConfig other)) return false;
        return yamlHandle.equals(other.getYamlHandle());
    }

    @Override
    public int hashCode() {
        return yamlHandle.hashCode();
    }
}
