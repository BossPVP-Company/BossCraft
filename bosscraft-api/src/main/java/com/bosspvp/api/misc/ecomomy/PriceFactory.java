package com.bosspvp.api.misc.ecomomy;

import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * Create prices.
 * <p>
 * Override create(PlaceholderContext, PlaceholderContextSupplier), other methods
 * are for backwards compatibility.
 */
public interface PriceFactory {
    /**
     * Get the names (how the price looks in lookup strings).
     * <p>
     * For example, for XP Levels this would be 'l', 'xpl', 'levels', etc.
     *
     * @return The allowed names.
     */
    @NotNull List<String> getNames();

    /**
     * Create the price.
     *
     * @param baseContext The base PlaceholderContext.
     * @param function    The function to use. Should use {@link PlaceholderContext#copyWithPlayer(Player)} on calls.
     * @return The price.
     */
    @NotNull Price create(@NotNull final PlaceholderContext baseContext,
                              @NotNull final Function<PlaceholderContext,Double> function);
}
