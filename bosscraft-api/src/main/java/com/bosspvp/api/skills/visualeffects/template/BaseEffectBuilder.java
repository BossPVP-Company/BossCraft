package com.bosspvp.api.skills.visualeffects.template;

import com.bosspvp.api.skills.visualeffects.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BaseEffectBuilder implements VisualEffectBuilder {
    private String id;
    private Supplier<VisualEffect> effectSupplier;
    private HashMap<String,Object> variables = new HashMap<>();
    private HashMap<String, VisualEffectVariable<?>> requiredVariables;


    public BaseEffectBuilder(String id, Supplier<VisualEffect> effectSupplier) {
        this.id = id;
        this.effectSupplier = effectSupplier;
        requiredVariables = effectSupplier.get().getVariables();
    }

    @Override
    @NotNull
    public <T> VisualEffectBuilder setOrigin(@NotNull VisualEffectLocation origin) {
        return setVariable("origin", origin);
    }

    @Override
    @NotNull public <T> VisualEffectBuilder setTarget(@NotNull VisualEffectLocation target) {
        return setVariable("target", target);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setDelay(int value) {
        return setVariable("delay", value);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setPeriod(int value) {
        return setVariable("period", value);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setIterations(int value) {
        return setVariable("iterations", value);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setRepeats(int value) {
        return setVariable("repeats", value);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setRepeatDelay(int value) {
        return setVariable("repeatDelay", value);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setDisplayRange(int value) {
        return setVariable("displayRange", value);
    }

    @Override
    public @NotNull <T> VisualEffectBuilder runManually(boolean flag) {
        return setVariable("runManually", flag);
    }
    @Override
    public @NotNull <T> VisualEffectBuilder runAsync(boolean flag) {
        return setVariable("async", flag);
    }

    @Override
    @NotNull public <T> VisualEffectBuilder setVariable(@NotNull String key,@NotNull T value) {
        variables.put(key, value);
        return this;
    }

    @Override
    public @NotNull HashMap<String, VisualEffectVariable<?>> getVariables() {
        return requiredVariables;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull VisualEffect build(@NotNull VisualEffectsManager manager) {
        VisualEffect effect = effectSupplier.get();
        for(Map.Entry<String,Object> entry: variables.entrySet()) {
            effect.setVariable(entry.getKey(), entry.getValue());
        }
        return effect;
    }
}
