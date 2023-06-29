package com.bosspvp.core.config;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.inventories.util.ItemBuilder;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
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
    public @Nullable Material getMaterialOrNull(@NotNull String path) {
        String mat = getStringOrNull(path);
        if(mat==null) return null;
        return Material.matchMaterial(mat.toUpperCase());
    }

    @Override
    public @NotNull Config getSubsection(@NotNull String path){
        return Objects.requireNonNullElse(getSubsectionOrNull(path), new EmptyConfig(yamlHandle,injections));
    }

    @Override
    public @Nullable Config getSubsectionOrNull(@NotNull String path) {
        Object obj = handle.get(path);
        if(obj==null) return null;
        if(obj instanceof ConfigurationSection section){
            BossConfig out = new BossConfig(yamlHandle,section);
            out.addInjectablePlaceholder(injections);
            return out;
        }else if(obj instanceof Map<?,?> map){
            YamlConfiguration configuration = new YamlConfiguration();
            Utils.applyMapToConfig(configuration, map);
            BossConfig out = new BossConfig(yamlHandle,configuration);
            out.addInjectablePlaceholder(injections);
            return out;
        }
        return null;
    }

    @Override
    public @Nullable List<Config> getSubsectionListOrNull(@NotNull String path) {
        Object obj = handle.get(path);
        if(obj==null) return null;
        if(obj instanceof ConfigurationSection mainSection){
            List<ConfigurationSection> list = mainSection.getKeys(false).stream()
                    .map(mainSection::getConfigurationSection).filter(Objects::nonNull).toList();
            List<Config> out = new ArrayList<>();
            list.forEach(it->{
                BossConfig config = new BossConfig(yamlHandle,it);
                config.addInjectablePlaceholder(injections);
                out.add(config);
            });
            return out;
        }else if(obj instanceof ArrayList<?> list){
            try {
                ArrayList<LinkedHashMap<String, Object>> sections = (ArrayList<LinkedHashMap<String, Object>>) list;
                List<Config> out = new ArrayList<>();
                sections.forEach(it->{
                    YamlConfiguration configuration = new YamlConfiguration();
                    Utils.applyMapToConfig(configuration, it);
                    BossConfig config = new BossConfig(yamlHandle,configuration);
                    config.addInjectablePlaceholder(injections);
                    out.add(config);
                });
                return out;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }

    @Override
    public double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context) {
        String text = getStringOrNull(path);
        if(text==null) return 0;

        PlaceholderContext context1 = context.withInjectableContext(this);
        return BossAPI.getInstance().evaluate(text,context1);
    }

    @Override
    public @Nullable ItemStack getItemStackOrNull(@NotNull String path) {
        Config config = getSubsectionOrNull(path);
        return getItemStack(config);
    }

    @Override
    public @Nullable List<ItemStack> getItemStackListOrNull(@NotNull String path) {
        List<Config> configs = getSubsectionListOrNull(path);
        if(configs==null) return null;
        List<ItemStack> out = new ArrayList<>();
        for(Config config : configs){
            ItemStack stack = getItemStack(config);
            if(stack==null) return null;
            out.add(stack);
        }
        return out;
    }
    @Nullable
    private ItemStack getItemStack(@Nullable Config config){
        if(config==null || (!config.hasPath("item") && !config.hasPath("icon"))) return null;

        ItemBuilder builder;
        if(config.hasPath("icon")){
            builder = new ItemBuilder(Material.PLAYER_HEAD)
                    .makeCustomSkull(config.getString("icon").split(":")[1]);
        }else {
            builder = new ItemBuilder(Objects.requireNonNullElse(
                    Material.matchMaterial(config.getString("item").toUpperCase()),
                    Material.BEDROCK
            ));
        }
        if(config.hasPath("name")) builder.setName(config.getFormattedString("name"));
        if(config.hasPath("lore")) builder.setLore(config.getFormattedStringList("lore"));
        if(config.hasPath("amount")) builder.setAmount(config.getInt("amount"));
        return builder.toItemStack();
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
