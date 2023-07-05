package com.bosspvp.core.config;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Returned on config subsection not found
 */
public class EmptyConfig implements Config {
    private Collection<InjectablePlaceholder> injections;
    private YamlConfiguration yamlHandle;
    public EmptyConfig(YamlConfiguration yamlHandle, Collection<InjectablePlaceholder> injections){
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
    public @Nullable Material getMaterialOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<Material> getMaterialListOrNull(@NotNull String path) {
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
    public @Nullable ItemStack getItemStackOrNull(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable List<ItemStack> getItemStackListOrNull(@NotNull String path) {
        return null;
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
        placeholders.forEach(it->{
            if(it==null) return;
            injections.add(it);
        });
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
    public @NotNull Collection<InjectablePlaceholder> getPlaceholderInjections() {
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
