package com.bosspvp.api.placeholders.types;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.RegistrablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;
public class PlayerlessPlaceholder implements RegistrablePlaceholder {
    /**
     * The arguments pattern.
     */
    private final Pattern pattern;

    /**
     * The function to retrieve the output of the arguments.
     */
    private final Supplier<@Nullable String> function;

    /**
     * The plugin for the arguments.
     */
    private final BossPlugin plugin;

    /**
     * Create a new player arguments.
     *
     * @param plugin     The plugin.
     * @param identifier The identifier.
     * @param function   The function to retrieve the value.
     */
    public PlayerlessPlaceholder(@NotNull final BossPlugin plugin,
                                 @NotNull final String identifier,
                                 @NotNull final Supplier<@Nullable String> function) {
        this.plugin = plugin;
        this.pattern = Pattern.compile(identifier);
        this.function = function;
    }

    @Override
    public @Nullable String getValue(@NotNull final String args,
                                     @NotNull final PlaceholderContext context) {
        return function.get();
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
    public @NotNull PlayerlessPlaceholder register() {
        return (PlayerlessPlaceholder) RegistrablePlaceholder.super.register();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerlessPlaceholder that)) {
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
