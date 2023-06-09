package com.bosspvp.api.skills.conditions.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.conditions.Condition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConditionInBlock extends Condition<Compilable.NoCompileData> {
    public ConditionInBlock(@NotNull BossPlugin plugin) {
        super(plugin,"in_block");
        setArguments(it->{
            it.require("block", "You must specify the block!");
        });
    }
    @Override
    public boolean isMet(Player player, Config config, Compilable.NoCompileData compileData) {
        var world = player.getWorld();
        var head = world.getBlockAt(player.getEyeLocation()).getType();
        var feet = world.getBlockAt(player.getEyeLocation().clone().subtract(0.0, 1.0, 0.0)).getType();
        var block = config.getString("block");
        return head.name().equalsIgnoreCase(block) || feet.name().equalsIgnoreCase(block);
    }
}
