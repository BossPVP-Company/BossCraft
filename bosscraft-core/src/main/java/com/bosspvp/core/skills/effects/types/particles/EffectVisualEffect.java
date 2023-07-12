package com.bosspvp.core.skills.effects.types.particles;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectBuilder;
import com.bosspvp.api.skills.visualeffects.VisualEffectVariable;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectLocation;
import com.bosspvp.core.skills.visualeffects.types.DynamicCircle;
import com.bosspvp.core.skills.visualeffects.types.Helix;
import com.bosspvp.core.skills.visualeffects.types.SingleParticle;
import com.bosspvp.core.skills.visualeffects.types.SnowFlake;
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
        VisualEffectsManager manager = getPlugin().getSkillsManager().getVisualEffectsManager();
        VisualEffectBuilder effectBuilder = manager
                .getEffectBuilder(config.getString("type").toLowerCase());
        if(effectBuilder==null) return false;
        effectBuilder
                .setOrigin(new BaseEffectLocation(data.location().clone()))
                .setTarget(new BaseEffectLocation(data.location().clone()))
                .setDelay(config.getIntOrDefault("delay",0))
                .setPeriod(config.getIntOrDefault("period",1))
                .setIterations(config.getIntOrDefault("iterations",1))
                .setRepeats(config.getIntOrDefault("repeats",0))
                .setRepeatDelay(config.getIntOrDefault("repeatDelay",0))
                .setDisplayRange(config.getIntOrDefault("displayRange",100))
                .runManually(config.getBool("runManually"));

        for(String varPath : effectBuilder.getExistingVariables()){
            if(config.hasPath(varPath)){
                effectBuilder.setVariable(varPath,config.getString(varPath));
            }
        }
        manager.startEffect(effectBuilder.build());
        return true;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.LOCATION);
    }
}
