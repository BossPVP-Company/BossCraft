package com.bosspvp.api.misc.ecomomy.types;

import com.bosspvp.api.misc.ecomomy.Price;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PriceItem implements Price {
    private final PlaceholderContext baseContext;

    private final Function<PlaceholderContext,Double> function;
    private final Material item;

    /**
     * The multipliers.
     */
    private final Map<UUID, Double> multipliers = new HashMap<>();

    public PriceItem(final int amount,
                     @NotNull final Material item) {
        this(PlaceholderContext.EMPTY, (PlaceholderContext ctx) -> (double) amount, item);
    }
    public PriceItem(@NotNull final PlaceholderContext baseContext,
                     @NotNull final Function<PlaceholderContext,Double> function,
                     @NotNull final Material item) {
        this.baseContext = baseContext;
        this.function = function;
        this.item = item;
    }

    /**
     * Get the item.
     *
     * @return The item.
     */
    public Material getItem() {
        return item;
    }

    @Override
    public boolean canAfford(@NotNull final Player player,
                             final double multiplier) {
        int toRemove = (int) getValue(player, multiplier);
        if (toRemove <= 0) {
            return true;
        }

        int count = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack!=null&&item==itemStack.getType()) {
                count += itemStack.getAmount();
            }
        }

        return count >= toRemove;
    }

    @Override
    public void pay(@NotNull final Player player,
                    final double multiplier) {
        int toRemove = (int) getValue(player, multiplier);
        int count = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (count >= toRemove) {
                break;
            }

            if (itemStack!=null && item == itemStack.getType()) {
                int itemAmount = itemStack.getAmount();

                if (itemAmount > toRemove) {
                    itemStack.setAmount(itemAmount - toRemove);
                }

                if (itemAmount <= toRemove) {
                    itemStack.setAmount(0);
                    itemStack.setType(Material.AIR);
                }

                count += itemAmount;
            }
        }
    }

    @Override
    public void giveTo(@NotNull final Player player,
                       final double multiplier) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.setAmount((int) getValue(player, multiplier));

        player.getInventory().addItem(itemStack);
    }

    @Override
    public double getValue(@NotNull final Player player,
                           final double multiplier) {
        return Math.toIntExact(Math.round(
                this.function.apply(baseContext.copyWithPlayer(player))
                        * getMultiplier(player) * multiplier
        ));
    }

    @Override
    public double getMultiplier(@NotNull final Player player) {
        return this.multipliers.getOrDefault(player.getUniqueId(), 1.0);
    }

    @Override
    public void setMultiplier(@NotNull final Player player,
                              final double multiplier) {
        this.multipliers.put(player.getUniqueId(), multiplier);
    }

    @Override
    public String getIdentifier() {
        return "eco:item-" + item.hashCode();
    }
}
