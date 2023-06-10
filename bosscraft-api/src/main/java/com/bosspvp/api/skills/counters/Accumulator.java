package com.bosspvp.api.skills.counters;

import org.bukkit.entity.Player;

public interface Accumulator {
    void accept(Player player, double count);
}
