package com.bosspvp.core.skills.conditions.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.conditions.Condition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConditionInBiome extends Condition<Compilable.NoCompileData> {
    public ConditionInBiome(@NotNull BossPlugin plugin) {
        super(plugin, "in_biome");
        setArguments((it)->{
            it.require("biomes", "You must specify the biomes!");
        });

    }

    @Override
    public boolean isMet(Player player, Config config, Compilable.NoCompileData compileData) {
        return config.getStringList("biomes").contains(
                player.getWorld().getBiome(
                        player.getLocation().getBlockX(),
                        player.getLocation().getBlockY(),
                        player.getLocation().getBlockZ()
                ).name().toLowerCase()
        );
    }
}
