package com.bosspvp.core.skills.conditions.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.conditions.Condition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConditionHasPermission extends Condition<Compilable.NoCompileData> {
    public ConditionHasPermission(@NotNull BossPlugin plugin) {
        super(plugin,"has_permission");
        setArguments(it->{
            it.require("permission", "You must specify the permission!");
        });
    }

    @Override
    public boolean isMet(Player player, Config config, NoCompileData compileData) {
        return player.hasPermission(config.getString("permission"));
    }

}
