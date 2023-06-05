package com.bosspvp.api.skills.holder.provided;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.effects.EffectBlock;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProvidedHolder {
    @NotNull Holder getHolder();

    @Nullable Object getProvider();

    default Set<EffectBlock> getActiveEffects(@NotNull Player player) {
        return getHolder().getEffectList().getList().stream()
                .filter(effectBlock -> effectBlock.getConditions().areMet(player, this))
                .collect(Collectors.toSet());
    }



    @AllArgsConstructor
    class SimpleProvidedHolder implements ProvidedHolder {
        @Getter
        @NotNull
        private Holder holder;
        @Getter
        @Nullable
        private Object provider;

        @Override
        public int hashCode() {
            return holder.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SimpleProvidedHolder providedHolder)) {
                return false;
            }
            return providedHolder.holder.equals(holder);
        }
    }

    @AllArgsConstructor
    class ProvidedHolderConfig implements Config {
        @Getter
        @NotNull
        private Config config;
        @Getter
        @NotNull
        private ProvidedHolder holder;

        @Override
        public double getEvaluated(@NotNull String path,
                                   @Nullable PlaceholderContext context) {

            return BossAPI.getInstance().evaluate(
                    config.getString(path),
                    context.withInjectableContext(config).copy((ItemStack) holder.getProvider())
            );
        }

        @Nullable
        public String getFormattedStringOrNull(@NotNull String path,
                                               @Nullable PlaceholderContext context) {
            String string = config.getStringOrNull(path);
            if (string == null) return null;
            return StringUtils.formatWithPlaceholders(
                    string,
                    context.withInjectableContext(config).copy((ItemStack) holder.getProvider())
            );
        }

        @Override
        public @NotNull List<String> getFormattedStringListOrNull(@NotNull String path,
                                                                  @Nullable PlaceholderContext context) {
            List<String> strings = config.getStringList(path);
            return StringUtils.formatWithPlaceholders(
                    strings,
                    context.withInjectableContext(config).copy((ItemStack) holder.getProvider())
            );
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String path) {
            return config.getStringList(path);
        }

        @Override
        public @Nullable List<String> getStringListOrNull(@NotNull String path) {
            return config.getStringListOrNull(path);
        }

        @Override
        public @Nullable Double getDoubleOrNull(@NotNull String path) {
            return config.getDoubleOrNull(path);
        }

        @Override
        public @Nullable List<Double> getDoubleListOrNull(@NotNull String path) {
            return config.getDoubleListOrNull(path);
        }

        @Override
        public @Nullable Config getSubsection(@NotNull String path) {
            return config.getSubsection(path);
        }

        @Override
        public @Nullable List<Config> getSubsectionListOrNull(@NotNull String path) {
            return config.getSubsectionListOrNull(path);
        }

        @Override
        public @Nullable String getStringOrNull(@NotNull String path) {
            return config.getStringOrNull(path);
        }

        @Override
        public @Nullable Integer getIntOrNull(@NotNull String path) {
            return config.getIntOrNull(path);
        }

        @Override
        public @Nullable List<Integer> getIntListOrNull(@NotNull String path) {
            return config.getIntListOrNull(path);
        }

        @Override
        public @Nullable Boolean getBoolOrNull(@NotNull String path) {
            return config.getBoolOrNull(path);
        }

        @Override
        public @Nullable List<Boolean> getBoolListOrNull(@NotNull String path) {
            return config.getBoolListOrNull(path);
        }

        @Override
        public boolean hasPath(@NotNull String path) {
            return config.hasPath(path);
        }

        @Override
        public @NotNull Set<String> getKeys(boolean deep) {
            return config.getKeys(deep);
        }

        @Override
        public @Nullable Object get(@NotNull String path) {
            return config.get(path);
        }

        @Override
        public void set(@NotNull String path, @Nullable Object obj) {
            config.set(path, obj);
        }

        @Override
        public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            config.addInjectablePlaceholder(placeholders);
        }

        @Override
        public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            config.removeInjectablePlaceholder(placeholders);
        }

        @Override
        public void clearInjectedPlaceholders() {
            config.clearInjectedPlaceholders();
        }

        @Override
        public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
            return config.getPlaceholderInjections();
        }

        @Override
        public @NotNull ConfigurationSection getHandle() {
            return config.getHandle();
        }

        @Override
        public @NotNull ConfigurationSection getYamlHandle() {
            return config.getYamlHandle();
        }
    }
}
