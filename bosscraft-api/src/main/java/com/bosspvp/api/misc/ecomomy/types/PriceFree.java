package com.bosspvp.api.misc.ecomomy.types;

import com.bosspvp.api.misc.ecomomy.Price;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PriceFree implements Price {
    @Override
    public boolean canAfford(@NotNull Player player, double multiplier) {
        return true;
    }

    @Override
    public void pay(@NotNull Player player, double multiplier) {

    }

    @Override
    public void giveTo(@NotNull Player player, double multiplier) {

    }

    @Override
    public double getValue(@NotNull Player player, double multiplier) {
        return 0;
    }

    @Override
    public double getMultiplier(@NotNull Player player) {
        return 1;
    }

    @Override
    public void setMultiplier(@NotNull Player player, double multiplier) {

    }

    @Override
    public String getIdentifier() {
        return "boss:free";
    }
}
