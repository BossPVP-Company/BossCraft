package com.bosspvp.api.skills.visualeffects.template;

import com.bosspvp.api.skills.visualeffects.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BaseEffectBuilder implements VisualEffectBuilder {
    private String id;
    private Supplier<VisualEffect> effectSupplier;
    private HashMap<String,String> stringVariables = new HashMap<>();
    private HashMap<String,Object> objectVariables = new HashMap<>();
    private List<String> existingVariables = new ArrayList<>();


    public BaseEffectBuilder(String id, Supplier<VisualEffect> effectSupplier) {
        this.id = id;
        this.effectSupplier = effectSupplier;
        existingVariables.addAll(effectSupplier.get().getVariables().keySet());
    }

    @Override
    @NotNull
    public VisualEffectBuilder setOrigin(@NotNull VisualEffectLocation origin) {
        return setVariableAsObject("origin", origin);
    }

    @Override
    @NotNull public VisualEffectBuilder setTarget(@NotNull VisualEffectLocation target) {
        return setVariableAsObject("target", target);
    }

    @Override
    public @NotNull VisualEffectBuilder setDelay(long value) {
        return setVariableAsObject("delay", value);
    }

    @Override
    public @NotNull VisualEffectBuilder setPeriod(long value) {
        return setVariableAsObject("period", value);
    }

    @Override
    public @NotNull VisualEffectBuilder setIterations(int value) {
        return setVariableAsObject("iterations", value);
    }

    @Override
    public @NotNull VisualEffectBuilder setRepeats(int value) {
        return setVariableAsObject("repeats", value);
    }

    @Override
    public @NotNull VisualEffectBuilder setRepeatDelay(int value) {
        return setVariableAsObject("repeatDelay", value);
    }

    @Override
    public @NotNull VisualEffectBuilder setDisplayRange(int value) {
        return setVariableAsObject("displayRange", value);
    }

    @Override
    public @NotNull VisualEffectBuilder runManually(boolean flag) {
        return setVariableAsObject("runManually", flag);
    }
    @Override
    public @NotNull VisualEffectBuilder runAsync(boolean flag) {
        return setVariableAsObject("async", flag);
    }

    @Override
    @NotNull public VisualEffectBuilder setVariable(@NotNull String key,@NotNull String value) {
        stringVariables.put(key, value);
        return this;
    }

    @Override
    public @NotNull <T> VisualEffectBuilder setVariableAsObject(@NotNull String key, @NotNull T value) {
        objectVariables.put(key, value);
        return this;
    }

    @Override
    public @NotNull List<String> getExistingVariables() {
        return existingVariables;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull VisualEffect build() {
        VisualEffect effect = effectSupplier.get();
        for(Map.Entry<String,Object> entry: objectVariables.entrySet()) {
            effect.setVariable(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String,String> entry: stringVariables.entrySet()) {
            VisualEffectVariable<?> variable = effect.getVariables().get(entry.getKey());
            if(variable == null) continue;
            variable.setValueFromString(entry.getValue());
        }
        return effect;
    }
}
