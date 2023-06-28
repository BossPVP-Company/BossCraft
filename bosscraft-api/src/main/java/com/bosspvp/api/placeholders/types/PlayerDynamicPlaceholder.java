package com.bosspvp.api.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.RegistrablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
public class PlayerDynamicPlaceholder implements RegistrablePlaceholder {
    /**
     * The arguments pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the arguments.
     */
    private final BiFunction<@NotNull String, @NotNull Player, @Nullable String> function;

    /**
     * The plugin for the arguments.
     */
    private final BossPlugin plugin;

    /**
     * Create a new dynamic arguments.
     *
     * @param plugin   The plugin.
     * @param pattern  The pattern.
     * @param function The function to retrieve the value.
     */
    public PlayerDynamicPlaceholder(@NotNull final BossPlugin plugin,
                                    @NotNull final Pattern pattern,
                                    @NotNull final BiFunction<@NotNull String, @NotNull Player, @Nullable String> function) {
        this.plugin = plugin;
        this.pattern = Pattern.compile("%"+plugin.getName().toLowerCase()+"_"+pattern.pattern()+"%");
        this.function = function;
    }

    @Override
    public @Nullable String getValue(@NotNull final String args,
                                     @NotNull final PlaceholderContext context) {
        Player player = context.getPlayer();

        if (player == null) {
            return null;
        }

        return function.apply(args, player);
    }
    @Override
    public @NotNull BossPlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    @Override
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public @NotNull PlayerDynamicPlaceholder register() {
        return (PlayerDynamicPlaceholder) RegistrablePlaceholder.super.register();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PlayerDynamicPlaceholder that)) {
            return false;
        }

        return Objects.equals(this.getPattern(), that.getPattern())
                && Objects.equals(this.getPlugin(), that.getPlugin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPattern(), this.getPlugin());
    }
}
