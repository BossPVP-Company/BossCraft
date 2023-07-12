package com.bosspvp.api.skills.visualeffects.template;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectVariable;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.tuples.PairRecord;
import com.bosspvp.api.utils.ParticleUtils;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseEffect implements VisualEffect {
    @Getter
    private String id = UUID.randomUUID().toString();
    @Getter
    @NotNull
    private final VisualEffectsManager effectsRegistry;

    @Getter
    boolean isAsync = true;

    @Getter
    private BaseEffect copy;

    @Getter
    private VisualEffectLocation origin = new BaseEffectLocation(new Location(null,0,0,0));
    @Getter
    private VisualEffectLocation target = new BaseEffectLocation(new Location(null,0,0,0));

    @Getter
    private boolean finished;
    @Getter
    private boolean runningManually;

    @Getter
    protected long delay = 0;
    @Getter
    protected long period = 1;
    @Getter
    protected int iterations = 1;

    @Getter
    protected int repeats = 0;
    @Getter
    protected int repeatDelay = 0;
    private int repeatDelayCount;

    @Getter
    private double displayRange = 100;

    @Getter
    private VisualEffectVariable<Particle> particleType = new BaseEffectVariable<>(Particle.REDSTONE,
            (it) -> Particle.valueOf(it.toUpperCase()));
    @Getter
    private VisualEffectVariable<Color> particleColor = new BaseEffectVariable<>(Color.WHITE,
            (it) -> {
                try {
                    String[] split = it.split(";");
                    if (split.length == 1)
                        return Color.fromRGB(Integer.parseInt(split[0]));
                    else if (split.length == 3) {
                        return Color.fromRGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    } else return Color.WHITE;
                } catch (Exception e) {
                    return Color.WHITE;
                }
            });
    @Getter
    private VisualEffectVariable<Material> particleMaterial = new BaseEffectVariable<>(Material.BARRIER,
            (it) -> Objects.requireNonNullElse(Material.matchMaterial(it.toUpperCase()), Material.BARRIER));
    @Getter
    private VisualEffectVariable<Float> particleSpeed = new BaseEffectVariable<>(0.0f,
            (it)->(float) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    @Getter
    private VisualEffectVariable<Integer> particleCount = new BaseEffectVariable<>(1,
            (it)->(int) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    @Getter
    private VisualEffectVariable<Double> particleOffsetX = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    @Getter
    private VisualEffectVariable<Double> particleOffsetY = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    @Getter
    private VisualEffectVariable<Double> particleOffsetZ = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );

    @Getter
    protected VisualEffectVariable<Double> thickness = new BaseEffectVariable<>(1.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );

    @Getter
    private HashMap<String, VisualEffectVariable<?>> variables;

    public BaseEffect(@NotNull VisualEffectsManager effectsManager) {
        this.effectsRegistry = effectsManager;
        loadVariables();
    }

    @Override
    public void run() {
        try {
            if (finished) return;
            //copy before first changes of variables
            if (repeats != 0 && copy == null && !runningManually) {
                copy = cloneWithSameId();
            }
            onRun();


            //repeater
            if (!runningManually) {
                if (repeats > 0 && repeatDelay != -1) {
                    repeatDelayCount--;
                    if (repeatDelayCount <= 0) {
                        BaseEffect craftEffect = copy.cloneWithSameId();
                        craftEffect.repeats = 0;
                        effectsRegistry.startEffect(craftEffect);

                        repeats--;
                        repeatDelayCount = repeatDelay;
                    }

                } else if (repeats == -1 && repeatDelay != -1) {
                    repeatDelayCount--;
                    if (repeatDelayCount <= 0) {
                        BaseEffect craftEffect = copy.cloneWithSameId();
                        craftEffect.repeats = 0;
                        effectsRegistry.startEffect(craftEffect);
                        repeatDelayCount = repeatDelay;
                    }
                }
            }

            if (iterations == -1) {
                return;
            }
            iterations--;
            if (iterations < 1) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancel(false);
        }

    }

    protected abstract void onRun();

    @Override
    public final void finish() {
        finished = true;
        effectsRegistry.finishEffect(this);
        onFinish();
        //repeat
        if (runningManually) return;

        if (repeats > 0 && repeatDelay == -1) {
            BaseEffect baseEffect = copy.cloneWithSameId();
            baseEffect.repeats = repeats - 1;
            effectsRegistry.startEffect(baseEffect);
        } else if (repeats == -1) {
            BaseEffect baseEffect = copy.cloneWithSameId();
            effectsRegistry.startEffect(baseEffect);
        }
    }

    protected abstract void onFinish();

    @Override
    public final void cancel(boolean callFinished) {
        if (callFinished) {
            finish();
        } else {
            finished = true;
            if (!isRunningManually()) {
                effectsRegistry.finishEffect(this);
            }
        }
    }


    @Override
    public void displayParticle(@NotNull Particle effect, @NotNull Location location) {
        displayParticle(effect, location, particleColor.getValue(), particleCount.getValue());
    }

    @Override
    public void displayParticle(@NotNull Particle particle, @NotNull Location location, Color color, int amount) {
        ParticleUtils.display(particle, location, particleOffsetX.getValue(), particleOffsetY.getValue(),
                particleOffsetZ.getValue(), particleSpeed.getValue(), amount,
                color, particleMaterial.getValue(), displayRange);
    }

    @Override
    public void displayParticle(@NotNull Particle particle, @NotNull Location location, Color color, float particleOffsetX,
                                float particleOffsetY,
                                float particleOffsetZ, int amount) {
        ParticleUtils.display(particle, location, particleOffsetX, particleOffsetY, particleOffsetZ, particleSpeed.getValue(), amount,
                color, particleMaterial.getValue(), displayRange);
    }

    @Override
    public final void runManually(boolean manually) {
        runningManually = manually;
    }

    private void loadVariables() {
        variables = new HashMap<>();
        variables.put("thickness", thickness);
        variables.put("particle.type", particleType);
        variables.put("particle.color", particleColor);
        variables.put("particle.material", particleMaterial);
        variables.put("particle.count", particleCount);
        variables.put("particle.speed", particleSpeed);
        variables.put("particle.offset.x", particleOffsetX);
        variables.put("particle.offset.y", particleOffsetY);
        variables.put("particle.offset.z", particleOffsetZ);

        for (PairRecord<String, VisualEffectVariable<?>> variable : origin.getVariables()) {
            variables.put("origin." + variable.first(), variable.second());
        }
        for(PairRecord<String,VisualEffectVariable<?>> variable : target.getVariables()){
            variables.put("target."+variable.first(),variable.second());
        }
    }

    @Override
    public <T> void setVariable(String key, T value) {
        switch (key){
            case "origin" ->{
                origin = (VisualEffectLocation) value;
                for(PairRecord<String,VisualEffectVariable<?>> variable : origin.getVariables()){
                    variables.put("origin."+variable.first(),variable.second());
                }
            }
            case "target" ->{
                target = (VisualEffectLocation) value;
                for(PairRecord<String,VisualEffectVariable<?>> variable : target.getVariables()){
                    variables.put("target."+variable.first(),variable.second());
                }
            }
            case "delay" ->{
                delay = (long) value;
            }
            case "period" ->{
                period = (long) value;
            }
            case "iterations" ->{
                iterations = (int) value;
            }
            case "repeats" ->{
                repeats = (int) value;
            }
            case "repeatDelay" ->{
                repeatDelay = (int) value;
            }
            case "displayRange" ->{
                displayRange = (int) value;
            }
            case "runManually" ->{
                runningManually = (boolean) value;
            }
            case "async" ->{
                isAsync = (boolean) value;
            }
            default -> {
                getEffectsRegistry().getPlugin().getLogger().info("Setting variable "+key+" to "+value);
                if(variables.containsKey(key)){
                    VisualEffectVariable<T> variable = (VisualEffectVariable<T>) variables.get(key);
                    variable.setValue(value);
                }
            }
        }
    }

    @Override
    public final BaseEffect clone() {
        try {
            BaseEffect baseEffect = (BaseEffect) (super.clone());
            baseEffect.id = UUID.randomUUID().toString();

            baseEffect.origin = origin.clone();
            if (baseEffect.target != null && target != null) {
                baseEffect.target = target.clone();
            }
            cloneVariables(baseEffect);
            onClone(baseEffect);

            baseEffect.loadVariables();
            return baseEffect;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public final BaseEffect cloneWithSameId() {
        BaseEffect baseEffect = clone();
        if(baseEffect == null) return null;
        baseEffect.id = id;
        return baseEffect;
    }

    private void cloneVariables(BaseEffect cloned) throws CloneNotSupportedException {
        cloned.particleType = particleType.clone();
        cloned.particleColor = particleColor.clone();
        cloned.particleMaterial = particleMaterial.clone();
        cloned.particleSpeed = particleSpeed.clone();
        cloned.particleCount = particleCount.clone();
        cloned.particleOffsetX = particleOffsetX.clone();
        cloned.particleOffsetY = particleOffsetY.clone();
        cloned.particleOffsetZ = particleOffsetZ.clone();
        cloned.thickness = thickness.clone();

    }

    protected abstract void onClone(BaseEffect cloned);

}
