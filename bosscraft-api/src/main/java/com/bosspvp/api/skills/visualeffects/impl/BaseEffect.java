package com.bosspvp.api.skills.visualeffects.impl;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectVariable;
import com.bosspvp.api.skills.visualeffects.VisualEffectsRegistry;
import com.bosspvp.api.tuples.PairRecord;
import com.bosspvp.api.utils.ParticleUtils;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseEffect implements VisualEffect {
    @Getter
    private String id = UUID.randomUUID().toString();
    @Getter
    @NotNull
    private final VisualEffectsRegistry effectsRegistry;

    @Getter
    boolean isAsync;

    @Getter
    private BaseEffect copy;

    @Getter
    @NotNull
    private VisualEffectLocation origin;
    @Getter
    @Nullable
    private VisualEffectLocation target;

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

    public BaseEffect(@NotNull VisualEffectsRegistry effectsManager,
                      @NotNull VisualEffectLocation origin,
                      @Nullable VisualEffectLocation target,
                      long period,
                      int iterations) {
        this.effectsRegistry = effectsManager;
        this.origin = origin;
        this.target = target;
        this.period = period;
        this.iterations = iterations;

        loadVariables();
    }

    public BaseEffect(@NotNull VisualEffectsRegistry effectsManager, @NotNull VisualEffectLocation origin) {
        this(effectsManager, origin, 1, 0);
    }
    public BaseEffect(@NotNull VisualEffectsRegistry effectsManager, @NotNull VisualEffectLocation origin, long period, int iterations) {
        this(effectsManager, origin, null, period, iterations);
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

    /**
     * set repeat delay
     */
    public void setRepeatDelay(int value) {
        repeatDelay = value;
        repeatDelayCount = value;
    }

    /**
     * set amount of effect repeats.
     */
    public void setRepeats(int value) {
        repeats = value;

    }

    /**
     * sets thickness that in most cases determines vector step,
     * per which particles will be displayed
     */
    public void setThickness(VisualEffectVariable<Double> value) {
        thickness = value;
    }

    /**
     * set default particle type
     */
    public final void setParticleType(VisualEffectVariable<Particle> value) {
        particleType = value;

    }

    /**
     * set default color of displaying particles
     */
    public void setParticleColor(VisualEffectVariable<Color> color) {
        particleColor = color;

    }

    /**
     * set default particle material
     */
    public final void setParticleMaterial(VisualEffectVariable<Material> value) {
        particleMaterial = value;

    }

    /**
     * set default particle count
     */
    public final void setParticleCount(VisualEffectVariable<Integer> value) {
        particleCount = value;

    }

    /**
     * set default particle speed
     */
    public final void setParticleSpeed(VisualEffectVariable<Float> value) {
        particleSpeed = value;

    }

    /**
     * set default particle offsetX
     */
    public final void setParticleOffsetX(VisualEffectVariable<Double> value) {
        particleOffsetX = value;

    }

    /**
     * set default particle offsetY
     */
    public final void setParticleOffsetY(VisualEffectVariable<Double> value) {
        particleOffsetY = value;

    }

    /**
     * set default particle offsetZ
     */
    public final void setParticleOffsetZ(VisualEffectVariable<Double> value) {
        particleOffsetZ = value;

    }

    /**
     * set display range of effect
     * default: 100
     */
    public final void setDisplayRange(double value) {
        displayRange = value;

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

        for(PairRecord<String,VisualEffectVariable<?>> variable : origin.getVariables()){
            variables.put("origin."+variable.first(),variable.second());
        }
        if(target == null) return;
        for(PairRecord<String,VisualEffectVariable<?>> variable : target.getVariables()){
            variables.put("target."+variable.first(),variable.second());
        }
    }

    @Override
    public <T> void setVariable(String key, T value) {
        if (variables.containsKey(key)) {
            VisualEffectVariable<T> variable = (VisualEffectVariable<T>) variables.get(key);
            variable.setValue(value);
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
