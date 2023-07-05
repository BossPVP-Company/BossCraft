package com.bosspvp.api.skills.visualeffects;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface VisualEffectBuilder {

    @NotNull <T> VisualEffectBuilder setOrigin(@NotNull VisualEffectLocation origin);
    @NotNull  <T> VisualEffectBuilder setTarget(@NotNull VisualEffectLocation target);

    @NotNull  <T> VisualEffectBuilder setDelay(int value);
    @NotNull  <T> VisualEffectBuilder setPeriod(int value);
    @NotNull  <T> VisualEffectBuilder setIterations(int value);

    @NotNull  <T> VisualEffectBuilder setRepeats(int value);
    @NotNull  <T> VisualEffectBuilder setRepeatDelay(int value);

    @NotNull  <T> VisualEffectBuilder setDisplayRange(int value);

    @NotNull  <T> VisualEffectBuilder runManually(boolean flag);
    @NotNull  <T> VisualEffectBuilder runAsync(boolean flag);

    @NotNull <T> VisualEffectBuilder setVariable(@NotNull String key,@NotNull  T value);
    @NotNull HashMap<String, VisualEffectVariable<?>> getVariables();

    @NotNull String getId();
    @NotNull VisualEffect build(@NotNull VisualEffectsManager manager);
}
