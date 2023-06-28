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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConfigMap implements Config {
    private List<InjectablePlaceholder> injections;
    private YamlConfiguration yamlHandle;

    private ConcurrentHashMap<String, Object> values;

    public ConfigMap(Map<String,?> map, YamlConfiguration yamlHandle, List<InjectablePlaceholder> injections){
        this.yamlHandle = yamlHandle;
        this.injections = injections;
        this.values = new ConcurrentHashMap<>(map);
    }

    @Override
    public boolean hasPath(@NotNull String path) {

        return get(path) != null;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return deep ? new HashSet<>(recurseKeys(new HashSet<>(), "")) : values.keySet();
    }

    @Override
    public List<String> recurseKeys(@NotNull Set<String> current, @NotNull String root){
        List<String> list = new ArrayList<>();
        for(String key : getKeys(false)){
            list.add(root + key);
            Object found = get(key);

            if(found instanceof Config){
                list.addAll(((Config) found).recurseKeys(current, root + key + "."));
            }

        }

        return list;
    }
    @Override
    public @Nullable Object get(@NotNull String path) {
        String nearestPath = path.split("\\.")[0];
        if(path.contains(".")){
            String remainingPath = path.replaceFirst(nearestPath + "\\.", "");
            if(remainingPath.isEmpty()){
                return null;
            }
            Object first = values.get(nearestPath);
            if(first instanceof Config){
                return ((Config) first).get(remainingPath);
            }
            if(first instanceof Map<?,?> map){
                try {
                    return new ConfigMap((Map<String, ?>) map, yamlHandle, injections).get(remainingPath);
                }catch (ClassCastException e){
                    return first;
                }
            }
        }
        return values.get(nearestPath);

    }

    @Override
    public void set(@NotNull String path, @Nullable Object obj) {
        String nearestPath = path.split("\\.")[0];
        if(path.contains(".")){
            String remainingPath = path.replaceFirst(nearestPath + "\\.", "");
            if(remainingPath.isEmpty()){
                return;
            }
            Config section = getSubsectionOrNull(nearestPath);
            ConfigurationSection yamlConfiguration;
            if(section == null){
                yamlConfiguration = new YamlConfiguration();
            }else{
                yamlConfiguration = section.getHandle();
            }
            yamlConfiguration.set(remainingPath, obj);
            values.put(nearestPath, yamlConfiguration);
        }
        /* val nearestPath = path.split(".")[0]

        if (path.contains(".")) {
            val remainingPath = path.removePrefix("${nearestPath}.")

            if (remainingPath.isEmpty()) {
                return
            }

            var section = getSubsectionOrNull(nearestPath) // Creates a section if null, therefore it can be set.
            if(section==null){
                section = AtumConfigSection(type)
            }
            section.set(remainingPath, obj)
            values[nearestPath] = section // Set the value
            return
        }

        if (obj == null) {
            values.remove(nearestPath)
        } else {
            values[nearestPath] = obj.constrainConfigTypes(type)
        }*/
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
    public @NotNull Config getSubsection(@NotNull String path) {
        return null;
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
}
