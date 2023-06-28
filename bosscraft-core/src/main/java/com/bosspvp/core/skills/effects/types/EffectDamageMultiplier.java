package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectDamageMultiplier extends Effect<Compilable.NoCompileData> {
    public EffectDamageMultiplier(@NotNull BossPlugin plugin) {
        super(plugin, "damage_multiplier");
        setArguments(it->{
                     it.require("multiplier", "You must specify the damage multiplier!");
                }
        );
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.EVENT);
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {

        if(!(data.event() instanceof EntityDamageEvent event)) return false;
        event.setDamage(event.getDamage() * config.getEvaluated("multiplier", data.toPlaceholderContext(config)));
        return true;
    }
}
