package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectRunCommand extends Effect<Compilable.NoCompileData> {
    public EffectRunCommand(@NotNull BossPlugin plugin) {
        super(plugin, "run_command");
        setArguments(it->{
            it.require("command", "You must specify the command to run!");
        });
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.PLAYER);
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        var player = data.player();
        if(player == null) return false;
        var victim = data.victim();
        var command = config.getString("command")
                .replace("%player%", player.getName());
        if(victim != null) {
            command = command.replace("%victim%", victim.getName());
        }
        command = StringUtils.formatWithPlaceholders(command, data.toPlaceholderContext(config));
        getPlugin().getServer().dispatchCommand(
                getPlugin().getServer().getConsoleSender(),
                command
        );
        return true;
    }
}
