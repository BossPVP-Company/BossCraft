package com.bosspvp.api.skills.counters;

import org.bukkit.entity.Player;

interface Accumulator {
    void accept(Player player, double count);
}
