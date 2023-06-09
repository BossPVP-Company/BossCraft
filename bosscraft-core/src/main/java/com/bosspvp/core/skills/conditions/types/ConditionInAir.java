package com.bosspvp.core.skills.conditions.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.conditions.Condition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class ConditionInAir extends Condition<Compilable.NoCompileData> {
    public ConditionInAir(@NotNull BossPlugin plugin) {
        super(plugin, "in_air");
    }

    @Override
    public boolean isMet(Player player, Config config, Compilable.NoCompileData compileData) {
        return player.getLocation().getBlock().isEmpty() == config.getBool("in_air");
    }

}
