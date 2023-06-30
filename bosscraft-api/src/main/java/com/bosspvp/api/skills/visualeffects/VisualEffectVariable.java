package com.bosspvp.api.skills.visualeffects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface VisualEffectVariable<T> extends Cloneable {
    /**
     * get the saved value
     *
     * @return The value
     */
    @NotNull
    T getValue();

    /**
     * set the value
     *
     * @return this
     */
    @NotNull
    VisualEffectVariable<T> setValue(@NotNull T value);

    /**
     * set the value
     *
     * @return this
     */
    @NotNull
    VisualEffectVariable<T> setValueFromString(@NotNull String value);

    VisualEffectVariable<T> clone() throws CloneNotSupportedException;
}
