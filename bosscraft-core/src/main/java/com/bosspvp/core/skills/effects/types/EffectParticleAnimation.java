package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectParticleAnimation extends Effect<Compilable.NoCompileData> {

    public EffectParticleAnimation(@NotNull BossPlugin plugin) {
        super(plugin, "animation");
        setArguments(it->{
            it.require("particle","You must specify the particle of the animation!");
        });

    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        if(data.location()==null || data.location().getWorld()==null) return false;
        ParticleUtils.display(Particle.valueOf(config.getStringOrDefault("particle","redstone").toUpperCase()),
                data.location(), 1.2, 1.2, 1.2, 0.5f, 92, Color.RED,null,100);
        return true;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.LOCATION);
    }
}
