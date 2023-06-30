package com.bosspvp.api.skills.visualeffects.impl;

import com.bosspvp.api.skills.visualeffects.ValueLimit;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class BaseValueLimit<T extends Number> implements ValueLimit<T> {
    @Getter private final T min;
    @Getter private final T max;
    public BaseValueLimit(T min, T max){
        this.min = min;
        this.max = max;
    }


    @NotNull
    @Override
    public T limit(@NotNull T value) {
        //@TODO
        return value;
    }

    @Override
    public ValueLimit<T> clone() throws CloneNotSupportedException {
        return null;
    }
}
