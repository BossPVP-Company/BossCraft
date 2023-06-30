package com.bosspvp.core.skills.effects.types.particles;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectVariable;
import com.bosspvp.api.skills.visualeffects.impl.BaseEffectLocation;
import com.bosspvp.api.skills.visualeffects.types.DynamicCircle;
import com.bosspvp.api.skills.visualeffects.types.Helix;
import com.bosspvp.api.skills.visualeffects.types.SingleParticle;
import com.bosspvp.api.skills.visualeffects.types.SnowFlake;
import com.bosspvp.api.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class EffectVisualEffect extends Effect<Compilable.NoCompileData> {

    public EffectVisualEffect(@NotNull BossPlugin plugin) {
        super(plugin, "visual_effect");
        setArguments(it->{
            it.require("type","You must specify the visual effect type!");
            it.require("particle","You must specify the particle type!");
        });

    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        if(data.location()==null || data.location().getWorld()==null) return false;
        VisualEffect effect;
        switch (config.getString("type").toLowerCase()){
            case "helix" ->{
                effect = new Helix(
                        getPlugin().getSkillsManager().getVisualEffectsRegistry(),
                        new BaseEffectLocation(data.location().clone()),
                        new BaseEffectLocation(data.location().clone()),
                        config.getIntOrDefault("period",1),
                        config.getIntOrDefault("iterations",1)
                );
            }
            case "snow_flake" ->{
                effect = new SnowFlake(
                        getPlugin().getSkillsManager().getVisualEffectsRegistry(),
                        new BaseEffectLocation(data.location()),
                        config.getIntOrDefault("period",1),
                        config.getIntOrDefault("iterations",1)
                );
            }
            case "dynamic_circle" -> {
                effect = new DynamicCircle(
                        getPlugin().getSkillsManager().getVisualEffectsRegistry(),
                        new BaseEffectLocation(data.location()),
                        config.getIntOrDefault("period",1),
                        config.getIntOrDefault("iterations",1)
                );
            }
            default -> {
                effect = new SingleParticle(
                        getPlugin().getSkillsManager().getVisualEffectsRegistry(),
                        new BaseEffectLocation(data.location()),
                        config.getIntOrDefault("period",1),
                        config.getIntOrDefault("iterations",1)
                );
            }
        }
        for(Map.Entry<String, VisualEffectVariable<?>> varPath : effect.getVariables().entrySet()){
            if(config.hasPath(varPath.getKey())){
                varPath.getValue().setValueFromString(config.getString(varPath.getKey()));
            }
        }
        getPlugin().getSkillsManager().getVisualEffectsRegistry().startEffect(effect);
        return true;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.LOCATION);
    }
}
