package com.bosspvp.api.skills.visualeffects;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public interface VisualEffectBuilder {

    @NotNull VisualEffectBuilder setOrigin(@NotNull VisualEffectLocation origin);
    @NotNull VisualEffectBuilder setTarget(@NotNull VisualEffectLocation target);

    @NotNull VisualEffectBuilder setDelay(long value);
    @NotNull VisualEffectBuilder setPeriod(long value);
    @NotNull VisualEffectBuilder setIterations(int value);

    @NotNull VisualEffectBuilder setRepeats(int value);
    @NotNull VisualEffectBuilder setRepeatDelay(int value);

    @NotNull VisualEffectBuilder setDisplayRange(int value);

    @NotNull VisualEffectBuilder runManually(boolean flag);
    @NotNull VisualEffectBuilder runAsync(boolean flag);

    @NotNull VisualEffectBuilder setVariable(@NotNull String key, @NotNull String value);
    @NotNull <T> VisualEffectBuilder setVariableAsObject(@NotNull String key, @NotNull T value);
    @NotNull List<String> getExistingVariables();

    @NotNull String getId();
    @NotNull VisualEffect build();
}
