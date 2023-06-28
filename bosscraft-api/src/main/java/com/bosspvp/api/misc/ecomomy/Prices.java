package com.bosspvp.api.misc.ecomomy;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.misc.ecomomy.types.PriceFree;
import com.bosspvp.api.misc.ecomomy.types.PriceItem;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Class to manage prices.
 */
public final class Prices {
    /**
     * All factories.
     */
    private static final Map<String, PriceFactory> FACTORIES = new ConcurrentHashMap<>();

    /**
     * Register a new price factory.
     *
     * @param factory The factory.
     */
    public static void registerPriceFactory(@NotNull final PriceFactory factory) {
        for (String name : factory.getNames()) {
            FACTORIES.put(name.toLowerCase(), factory);
        }
    }

    /**
     * Create price from an expression (representing the value),
     * and a price name.
     * <p>
     * Supports items as price names.
     *
     * @param expression The expression for the value.
     * @param priceName  The price name.
     * @return The price, or free if invalid.
     */
    @NotNull
    public static Price create(@NotNull final String expression,
                               @Nullable final String priceName) {
        return create(expression, priceName, PlaceholderContext.EMPTY);
    }

    /**
     * Create price from an expression (representing the value),
     * and a price name. Uses a context to parse the expression.
     * <p>
     * Supports items as price names.
     *
     * @param expression The expression for the value.
     * @param priceName  The price name.
     * @param context    The math context to parse the expression.
     * @return The price, or free if invalid.
     */
    @NotNull
    public static Price create(@NotNull final String expression,
                               @Nullable final String priceName,
                               @NotNull final PlaceholderContext context) {
        Function<PlaceholderContext,Double> function = (ctx) -> BossAPI.getInstance().evaluate(
                expression,
                ctx
        );
        if(priceName == null) {
            return new PriceFree();
        }
        PriceFactory factory = FACTORIES.get(priceName);
        if(factory == null) {
            Material material = Material.matchMaterial(priceName);
            if(material != null) {
                return new PriceItem(context,function,material);
            }
            return new PriceFree();
        } else {
            return factory.create(context, function);
        }
    }

    private Prices() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
