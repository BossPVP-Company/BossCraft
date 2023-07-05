package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectBleed extends Effect<Compilable.NoCompileData> {
    public EffectBleed(@NotNull BossPlugin plugin) {
        super(plugin, "bleed");
        setArguments(it->{
                    it.require("amount", "You must specify the amount of bleed ticks!");
                    it.require("damage", "You must specify the amount of damage to deal!");
                    it.require("interval", "You must specify the tick delay between damages!");

        });

    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        var victim = data.victim();
        if(victim == null) return false;
        double damage = config.getEvaluated("damage", data.toPlaceholderContext(config));
        int interval = (int) config.getEvaluated("interval", data.toPlaceholderContext(config));
        int amount = (int) config.getEvaluated("amount", data.toPlaceholderContext(config));
        int[] current = {0};
        BukkitTask[] task = {null};
        task[0] = getPlugin().getScheduler().runTimer(interval, interval, ()->{
            current[0]++;
            if(damage >= victim.getHealth()) {
                victim.setKiller(data.player());
            }
            victim.damage(damage);
            if(current[0] >= amount) {
                task[0].cancel();
            }
        });
        return true;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.VICTIM);
    }
}
