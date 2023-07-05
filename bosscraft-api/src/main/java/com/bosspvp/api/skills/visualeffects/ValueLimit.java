package com.bosspvp.api.skills.visualeffects;

import org.jetbrains.annotations.NotNull;

public interface ValueLimit<T> extends Cloneable{
    /**
     * getEffectBuilder the max value
     *
     * @return The value
     */
    @NotNull
    T getMax();

    /**
     * getEffectBuilder the min value
     *
     * @return The value
     */
    @NotNull
    T getMin();

    /**
     * limit the given value
     *
     * @param value to limit
     * @return The result
     */
    @NotNull
    T limit(@NotNull T value);

    ValueLimit<T> clone() throws CloneNotSupportedException;
}
